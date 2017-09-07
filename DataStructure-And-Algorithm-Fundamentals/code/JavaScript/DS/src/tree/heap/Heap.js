// @flow

function CMP(l, r) {
  return l - r;
}

function MinHeap(scoreFn) {
  this.cmp = scoreFn || CMP;
  this.heap = [];
  this.size = 0;
}

MinHeap.prototype = {
  insert: function(item) {
    var heap = this.heap,
      ix = this.size++;

    heap[ix] = item;

    var parent = (ix - 1) >> 1;

    while (ix > 0 && this.cmp(heap[parent], item) > 0) {
      var tmp = heap[parent];
      heap[parent] = heap[ix];
      heap[ix] = tmp;
      ix = parent;
      parent = (ix - 1) >> 1;
    }
  },

  removeHead: function() {
    var heap = this.heap,
      cmp = this.cmp;

    if (this.size === 0) return undefined;

    var out = heap[0];

    this._bubble(0);

    return out;
  },

  remove: function(item) {
    var heap = this.heap;

    for (var i = 0; i < this.size; ++i) {
      if (heap[i] === item) {
        this._bubble(i);
        return true;
      }
    }

    return false;
  },

  _bubble: function(ix) {
    var heap = this.heap,
      cmp = this.cmp;

    heap[ix] = heap[--this.size];
    heap[this.size] = null;

    while (true) {
      var leftIx = (ix << 1) + 1,
        rightIx = (ix << 1) + 2,
        minIx = ix;

      if (leftIx < this.size && cmp(heap[leftIx], heap[minIx]) < 0) {
        minIx = leftIx;
      }

      if (rightIx < this.size && cmp(heap[rightIx], heap[minIx]) < 0) {
        minIx = rightIx;
      }

      if (minIx !== ix) {
        var tmp = heap[ix];
        heap[ix] = heap[minIx];
        heap[minIx] = tmp;
        ix = minIx;
      } else {
        break;
      }
    }
  }
};

module.exports = MinHeap;
