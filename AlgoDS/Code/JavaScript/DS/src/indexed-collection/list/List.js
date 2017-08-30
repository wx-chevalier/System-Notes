/*** ===================================================================== ***\
 ** - LISTS --------------------------------------------------------------- **
 * ========================================================================= *
 *                  *     _______________________                            *
 *                    ()=(_______________________)=()           *            *
 *       *                |                     |                            *
 *                        |   ~ ~~~~~~~~~~~~~   |       *               *    *
 *             *          |                     |                            *
 *   *                    |   ~ ~~~~~~~~~~~~~   |         *                  *
 *                        |                     |                            *
 *                        |   ~ ~~~~~~~~~~~~~   |                 *          *
 *        *               |                     |                            *
 *                   *    |_____________________|         *        *         *
 *                    ()=(_______________________)=()                        *
 **                                                                         **
\*** ===================================================================== ***/

/**
 * To demonstrate the raw interaction between memory and a data structure we're
 * going to first implement a list.
 *
 * A list is a representation of an ordered sequence of values where the same
 * value may appear many times.
 */

class List {
  /**
   * We start with an empty block of memory which we are going to represent
   * with a normal JavaScript array and we'll store the length of the list.
   *
   * Note that we want to store the length separately because in real life the
   * "memory" doesn't have a length you can read from.
   */

  constructor() {
    this.memory = [];
    this.length = 0;
  }

  /**
   * First we need a way to retrieve data from our list.
   *
   * With a plain list, you have very fast memory access because you keep track
   * of the address directly.
   *
   * List access is constant O(1) - "AWESOME!!"
   */

  get(address) {
    return this.memory[address];
  }

  /**
   * Because lists have an order you can insert stuff at the start, middle,
   * or end of them.
   *
   * For our implementation we're going to focus on adding and removing values
   * at the start or end of our list with these four methods:
   *
   *   - Push    - Add value to the end
   *   - Pop     - Remove value from the end
   *   - Unshift - Add value to the start
   *   - Shift   - Remove value from the start
   */

  /*
   * Starting with "push" we need a way to add items to the end of the list.
   *
   * It is as simple as adding a value in the address after the end of our
   * list. Because we store the length this is easy to calculate. We just add
   * the value and increment our length.
   *
   * Pushing an item to the end of a list is constant O(1) - "AWESOME!!"
   */

  push(value) {
    this.memory[this.length] = value;
    this.length++;
  }

  /**
   * Next we need a way to "pop" items off of the end of our list.
   *
   * Similar to push all we need to do is remove the value at the address at
   * the end of our list. Then just decrement length.
   *
   * Popping an item from the end of a list is constant O(1) - "AWESOME!!"
   */

  pop() {
    // Don't do anything if we don't have any items.
    if (this.length === 0) return;

    // Get the last value, stop storing it, and return it.
    var lastAddress = this.length - 1;
    var value = this.memory[lastAddress];
    delete this.memory[lastAddress];
    this.length--;

    // Also return the value so it can be used.
    return value;
  }

  /**
   * "push" and "pop" both operate on the end of a list, and overall are pretty
   * simple operations because they don't need to be concerned with the rest of
   * the list.
   *
   * Let's see what happens when we operate on the begining of the list with
   * "unshift" and "shift".
   */

  /**
   * In order to add a new item at the beginning of our list, we need to make
   * room for our value at the start by sliding all of the values over by one.
   *
   *     [a, b, c, d, e]
   *      0  1  2  3  4
   *       ⬊  ⬊  ⬊  ⬊  ⬊
   *         1  2  3  4  5
   *     [x, a, b, c, d, e]
   *
   * In order to slide all of the items over we need to iterate over each one
   * moving the prev value over.
   *
   * Because we have to iterate over every single item in the list:
   *
   * Unshifting an item to the start of a list is linear O(N) - "OKAY."
   */

  unshift(value) {
    // Store the value we are going to add to the start.
    var previous = value;

    // Iterate through each item...
    for (var address = 0; address < this.length; address++) {
      // replacing the "current" value with the "previous" value and storing the
      // "current" value for the next iteration.
      var current = this.memory[address];
      this.memory[address] = previous;
      previous = current;
    }

    // Add the last item in a new position at the end of the list.
    this.memory[this.length] = previous;
    this.length++;
  }

  /**
   * Finally, we need to write a shift function to move in the opposite
   * direction.
   *
   * We delete the first value and then slide through every single item in the
   * list to move it down one address.
   *
   *     [x, a, b, c, d, e]
   *         1  2  3  4  5
   *       ⬋  ⬋  ⬋  ⬋  ⬋
   *      0  1  2  3  4
   *     [a, b, c, d, e]
   *
   * Shifting an item from the start of a list is linear O(N) - "OKAY."
   */

  shift() {
    // Don't do anything if we don't have any items.
    if (this.length === 0) return;

    var value = this.memory[0];

    // Iterate through each item...
    for (var address = 0; address < this.length; address++) {
      // and replace them with the next item in the list.
      this.memory[address] = this.memory[address + 1];
    }

    // Delete the last item since it is now in the previous address.
    delete this.memory[this.length - 1];
    this.length--;

    return value;
  }
}

/**
 * Lists are great for fast access and dealing with items at the end. However,
 * as we've seen it isn't great at dealing with items not at the end of the
 * list and we have to manually hold onto memory addresses.
 *
 * So let's take a look at a different data structure and how it deals with
 * adding, accessing, and removing values without needing to know memory
 * addresses.
 */

/*** ===================================================================== ***\
 ** - HASH TABLES --------------------------------------------------------- **
 * ========================================================================= *
 *                           ((\                                             *
 *     (              _  ,-_  \ \                                            *
 *     )             / \/  \ \ \ \                                           *
 *     (            /)| \/\ \ \| |          .'---------------------'.        *
 *     `~()_______)___)\ \ \ \ \ |        .'                         '.      *
 *                 |)\ )  `' | | |      .'-----------------------------'.    *
 *                /  /,          |      '...............................'    *
 *        ejm     |  |          /         \   _____________________   /      *
 *                \            /           | |_)                 (_| |       *
 *                 \          /            | |                     | |       *
 *                  )        /             | |                     | |       *
 **                /       /              (___)                   (___)     **
\*** ===================================================================== ***/

/**
 * Hash tables are an *unordered*, instead we have "keys" and "values" where we
 * computed an address in memory using the key.
 *
 * The basic idea is that we have keys that are "hashable" (which we'll get to
 * in a second) and can by used to add, access, and remove from memory very
 * efficiently.
 *
 *     var hashTable = new HashTable();
 *
 *     hashTable.set('myKey', 'myValue');
 *     hashTable.get('myKey'); // >> 'myValue'
 */

class HashTable {
  /**
   * Again we're going to use a plain JavaScript array to represent our memory.
   */

  constructor() {
    this.memory = [];
  }

  /**
   * In order to store key-value pairs in memory from our hash table we need a
   * way to take the key and turn it into an address. We do this through an
   * operation known as "hashing".
   *
   * Basically it takes a key and serializes it into a unique number for that
   * key.
   *
   *    hashKey("abc") =>  96354
   *    hashKey("xyz") => 119193
   *
   * You have to be careful though, if you had a really big key you don't want
   * to match it to a memory address that does not exist.
   *
   * So the hashing algorithm needs to limit the size, which means that there
   * are a limited number of addresses for an unlimited number of values.
   *
   * The result is that you can end up with collisions. Places where two keys
   * get turned into the same address.
   *
   * Any real world hash table implementation would have to deal with this,
   * however we are just going to glaze over it and pretend that doesn't happen.
   */

  /**
   * Let's setup our "hashKey" function.
   *
   * Don't worry about understanding the logic of this function, just know that
   * it accepts a string and outputs a (mostly) unique address that we will use
   * in all of our other functions.
   */

  hashKey(key) {
    var hash = 0;
    for (var index = 0; index < key.length; index++) {
      // Oh look– magic.
      var code = key.charCodeAt(index);
      hash = ((hash << 5) - hash + code) | 0;
    }
    return hash;
  }

  /**
   * Next, lets define our "get" function so we have a way of accessing values
   * by their key.
   *
   * HashTable access is constant O(1) - "AWESOME!!"
   */

  get(key) {
    // We start by turning our key into an address.
    var address = this.hashKey(key);
    // Then we simply return whatever is at that address.
    return this.memory[address];
  }

  /**
   * We also need a way of adding data before we access it, so we will create
   * a "set" function that inserts values.
   *
   * HashTable setting is constant O(1) - "AWESOME!!"
   */

  set(key, value) {
    // Again we start by turning the key into and address.
    var address = this.hashKey(key);
    // Then just set the value at that address.
    this.memory[address] = value;
  }

  /**
   * Finally we just need a way to remove items from our hash table.
   *
   * HashTable deletion is constant O(1) - "AWESOME!!"
   */

  remove(key) {
    // As always, we hash the key to get an address.
    var address = this.hashKey(key);
    // Then, if it exists, we `delete` it.
    if (this.memory[address]) {
      delete this.memory[address];
    }
  }
}

module.exports = List;
