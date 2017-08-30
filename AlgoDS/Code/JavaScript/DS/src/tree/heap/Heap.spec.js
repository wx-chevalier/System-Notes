var test = require("tape");
var MinHeap = require("../");

function t(str, fn) {
  test(str, function(t) {
    fn(t);
    t.end();
  });
}

function testRemoval(heap, t) {
  var last = -Infinity;
  while (heap.size) {
    var head = heap.removeHead();
    t.ok(head >= last);
    last = head;
  }
}

t("initial .size", function(t) {
  var heap = new MinHeap();
  t.equal(heap.size, 0);
});

t("size after 100 inserts", function(t) {
  var heap = new MinHeap();
  for (var i = 0; i < 100; ++i) {
    heap.insert(Math.random());
  }
  t.equal(heap.size, 100);
});

t("fuzz test", function(t) {
  var heap = new MinHeap();
  var check = [];

  for (var i = 0; i < 50; ++i) {
    var v = Math.random();
    heap.insert(v);
    check.push(v);
  }

  check.sort();
  for (var i = 0; i < 20; ++i) {
    t.ok(heap.removeHead() === check.shift());
  }

  for (var i = 0; i < 50; ++i) {
    var v = Math.random();
    heap.insert(v);
    check.push(v);
  }

  check.sort();
  while (heap.size) {
    t.ok(heap.removeHead() === check.shift());
  }

  t.ok(check.length === 0);
});

t("remove item", function(t) {
  var heap = new MinHeap();

  heap.insert(4);
  heap.insert(1);
  heap.insert(3);
  heap.insert(5);

  t.ok(heap.remove(3));
  t.equal(heap.size, 3);

  testRemoval(heap, t);
});

t("remove biggest item", function(t) {
  var heap = new MinHeap();

  heap.insert(1);
  heap.insert(2);
  heap.insert(3);
  heap.insert(4);
  heap.insert(5);

  t.ok(heap.remove(5));
  t.equal(heap.size, 4);

  testRemoval(heap, t);
});

t("remove non-existant item", function(t) {
  var heap = new MinHeap();

  heap.insert(1);
  heap.insert(2);
  heap.insert(3);

  t.notOk(heap.remove(4));
  t.equal(heap.size, 3);
});

t("user defined scoring function", function(t) {
  var heap = new MinHeap(function(l, r) {
    return l.weight - r.weight;
  });

  heap.insert({ weight: 1, id: "spaghetti" });
  heap.insert({ weight: 3, id: "courgettes" });

  t.equal(heap.removeHead().id, "spaghetti");
  t.equal(heap.removeHead().id, "courgettes");
});
