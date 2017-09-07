package wx.algorithm.search.bt;

import java.util.Scanner;

/**
 * Created by apple on 16/7/30.
 */
public class BTreeTest {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n, n2, temp;
        System.out.print("Enter the t of the Tree?  ");
        n = input.nextInt();

        while (n < 2) {
            System.out.print("Please enter a integer greater than 1 : ");
            n = input.nextInt();//  User inputs the order of Tree and is assinged to N.
        }
        BTree tree = new BTree(n);//  B-Tree Tree with order  N is created.

        // Initial Values are added to the Tree.. The user and Input any number of Values.
        System.out.print("\n How many values do you want to enter?:  ");
        n2 = input.nextInt();

        for (int i = 0; i < n2; i++) {
            System.out.print("\nEnter Value:");
            System.out.println(i + 1);
            temp = input.nextInt();
            tree.insert(tree, temp);
        }
        int choice, k;// Variables used to control the Repeated loop of the MENU.

        boolean flag;
        flag = true;
        System.out.println("\tM\tE\tN\tU\n");
        System.out.println("1. Enter more values in a Tree");
        System.out.println("2. Print the whole  Tree in preorder");
        System.out.println("3. Search for a Key and print the Node it belongs to");
        System.out.println("4. Delete a key from the leaf");
        System.out.println("5. Exit");

        while (flag)// This While loop runs as long as the user enters anything other than a 5.
        {


            System.out.print("\nPlease enter your choice::");
            choice = input.nextInt();
            if (choice == 5) {
                System.out.printf("The program is exiting...,\n"
                        + "Thank you for using B-Tree implementation in Java\n"
                        + " Authors: Jeremy Phelps and Krish Unnikannan");
                System.exit(0);
                flag = false;
                break;
            } else {
                switch (choice) {
                    case 1: //If the User Enters 1 this case is executed and
                        //its function is to Enter more values in a Tree.

                        System.out.print("How many values do you want to enter?:");
                        n2 = input.nextInt();

                        for (int i = 0; i < n2; i++) {
                            System.out.print("\nEnter Value: ");
                            System.out.println(i + 1);
                            temp = input.nextInt();
                            tree.insert(tree, temp);
                        }
                        break;

                    case 2: //If the User Enters 2 this case is executed and
                        //its function is to Print the whole  Tree in preorder format.

                        tree.print(tree.root);
                        System.out.println();
                        break;

                    case 3: //If the User Enters 3 this case is executed and
                        //its function is to Delete a key from the leaf

                        System.out.println("What is the key you wish to search for:");
                        int key2 = input.nextInt();
//                        tree.SearchPrintNode(tree, key2);

                        break;
                    case 4: //If User Enters 4, this case is executed
                        //Its Function is to search for a Key and print the Node it belongs to

                        System.out.println("Enter a key to be deleted:");
                        int key = input.nextInt();
                        tree.deleteKey(tree, key);
                        System.out.println("Here is the tree printed in preorder after delete");
                        tree.print(tree.root);
                        break;

                    case 5: //If the User Enters 5, this case is executed and
                        //its function is to Exit

                        System.exit(0);
                        break;

                    default: // If the User enters a wrong choice, then this case is executed.
                        System.out.println("\nPlease enter a valid choice of 1,2,3 or 4\n");
                        break;
                }//end of switch block
            }//end of else block
        }//end while(flag)
    }//main
}
