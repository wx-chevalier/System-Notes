// classDataArray.java
// data items as class objects
// to run this program: C>java ClassDataApp
////////////////////////////////////////////////////////////////
class Person
   {
   private String lastName;
   private String firstName;
   private int age;
//--------------------------------------------------------------
   public Person(String last, String first, int a)
      {                               // constructor
      lastName = last;
      firstName = first;
      age = a;
      }
//--------------------------------------------------------------
   public void displayPerson()
      {
      System.out.print("   Last name: " + lastName);
      System.out.print(", First name: " + firstName);
      System.out.println(", Age: " + age);
      }
//--------------------------------------------------------------
   public String getLast()           // get last name
      { return lastName; }
   }  // end class Person
////////////////////////////////////////////////////////////////
class ClassDataArray
   {
   private Person[] a;               // reference to array
   private int nElems;               // number of data items

   public ClassDataArray(int max)    // constructor
      {
      a = new Person[max];               // create the array
      nElems = 0;                        // no items yet
      }
//--------------------------------------------------------------
   public Person find(String searchName)
      {                              // find specified value
      int j;
      for(j=0; j<nElems; j++)            // for each element,
         if( a[j].getLast().equals(searchName) )  // found item?
            break;                       // exit loop before end
      if(j == nElems)                    // gone to end?
         return null;                    // yes, can't find it
      else
         return a[j];                    // no, found it
      }  // end find()
//--------------------------------------------------------------                                    // put person into array
   public void insert(String last, String first, int age)
      {
      a[nElems] = new Person(last, first, age);
      nElems++;                          // increment size
      }
//--------------------------------------------------------------
   public boolean delete(String searchName)
      {                              // delete person from array
      int j;
      for(j=0; j<nElems; j++)            // look for it
         if( a[j].getLast().equals(searchName) )
            break;
      if(j==nElems)                      // can't find it
         return false;
      else                               // found it
         {
         for(int k=j; k<nElems; k++)     // shift down
            a[k] = a[k+1];
         nElems--;                       // decrement size
         return true;
         }
      }  // end delete()
//--------------------------------------------------------------
   public void displayA()            // displays array contents
      {
      for(int j=0; j<nElems; j++)       // for each element,
         a[j].displayPerson();          // display it
      }
//--------------------------------------------------------------
   }  // end class ClassDataArray
////////////////////////////////////////////////////////////////
class ClassDataApp
   {
   public static void main(String[] args)
      {
      int maxSize = 100;             // array size
      ClassDataArray arr;            // reference to array
      arr = new ClassDataArray(maxSize);  // create the array
                                     // insert 10 items
      arr.insert("Evans", "Patty", 24);
      arr.insert("Smith", "Lorraine", 37);
      arr.insert("Yee", "Tom", 43);
      arr.insert("Adams", "Henry", 63);
      arr.insert("Hashimoto", "Sato", 21);
      arr.insert("Stimson", "Henry", 29);
      arr.insert("Velasquez", "Jose", 72);
      arr.insert("Lamarque", "Henry", 54);
      arr.insert("Vang", "Minh", 22);
      arr.insert("Creswell", "Lucinda", 18);

      arr.displayA();                // display items

      String searchKey = "Stimson";  // search for item
      Person found;
      found=arr.find(searchKey);
      if(found != null)
         {
         System.out.print("Found ");
         found.displayPerson();
         }
      else
         System.out.println("Can't find " + searchKey);

      System.out.println("Deleting Smith, Yee, and Creswell");
      arr.delete("Smith");           // delete 3 items
      arr.delete("Yee");
      arr.delete("Creswell");

      arr.displayA();                // display items again
      }  // end main()
   }  // end class ClassDataApp
