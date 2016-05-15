// mergeSort.java
// demonstrates recursive merge sort
// to run this program: C>java MergeSortApp
////////////////////////////////////////////////////////////////
class DArray
   {
   private long[] theArray;          // ref to array theArray
   private int nElems;               // number of data items
   //-----------------------------------------------------------
   public DArray(int max)            // constructor
      {
      theArray = new long[max];      // create array
      nElems = 0;
      }
   //-----------------------------------------------------------
   public void insert(long value)    // put element into array
      {
      theArray[nElems] = value;      // insert it
      nElems++;                      // increment size
      }
   //-----------------------------------------------------------
   public void display()             // displays array contents
      {
      for(int j=0; j<nElems; j++)    // for each element,
         System.out.print(theArray[j] + " ");  // display it
      System.out.println("");
      }
   //-----------------------------------------------------------
   public void mergeSort()           // called by main()
      {                              // provides workspace
      long[] workSpace = new long[nElems];
      recMergeSort(workSpace, 0, nElems-1);
      }
   //-----------------------------------------------------------
   private void recMergeSort(long[] workSpace, int lowerBound,
                                               int upperBound)
      {
      if(lowerBound == upperBound)            // if range is 1,
         return;                              // no use sorting
      else
         {                                    // find midpoint
         int mid = (lowerBound+upperBound) / 2;
                                              // sort low half
         recMergeSort(workSpace, lowerBound, mid);
                                              // sort high half
         recMergeSort(workSpace, mid+1, upperBound);
                                              // merge them
         merge(workSpace, lowerBound, mid+1, upperBound);
         }  // end else
      }  // end recMergeSort()
   //-----------------------------------------------------------
   private void merge(long[] workSpace, int lowPtr,
                           int highPtr, int upperBound)
      {
      int j = 0;                             // workspace index
      int lowerBound = lowPtr;
      int mid = highPtr-1;
      int n = upperBound-lowerBound+1;       // # of items

      while(lowPtr <= mid && highPtr <= upperBound)
         if( theArray[lowPtr] < theArray[highPtr] )
            workSpace[j++] = theArray[lowPtr++];
         else
            workSpace[j++] = theArray[highPtr++];

      while(lowPtr <= mid)
         workSpace[j++] = theArray[lowPtr++];

      while(highPtr <= upperBound)
         workSpace[j++] = theArray[highPtr++];

      for(j=0; j<n; j++)
         theArray[lowerBound+j] = workSpace[j];
      }  // end merge()
   //-----------------------------------------------------------
   }  // end class DArray
////////////////////////////////////////////////////////////////
class MergeSortApp
   {
   public static void main(String[] args)
      {
      int maxSize = 100;             // array size
      DArray arr;                    // reference to array
      arr = new DArray(maxSize);     // create the array

      arr.insert(64);                // insert items
      arr.insert(21);
      arr.insert(33);
      arr.insert(70);
      arr.insert(12);
      arr.insert(85);
      arr.insert(44);
      arr.insert(3);
      arr.insert(99);
      arr.insert(0);
      arr.insert(108);
      arr.insert(36);

      arr.display();                 // display items

      arr.mergeSort();               // merge sort the array

      arr.display();                 // display items again
      }  // end main()
   }  // end class MergeSortApp
////////////////////////////////////////////////////////////////
