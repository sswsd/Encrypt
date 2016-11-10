import java.io.*;
import java.util.Scanner;

/**
 * Created by danwang on 15/6/15.
 */
public class Encrypt
{
    private static String algorithm = "DES";

    public boolean hasValidParams(String[] args)
    {
        boolean returnValue = true;

        System.out.println(args.length);
        if (args.length != 3)
        {
            System.out.println("Wrong parameter!");
            returnValue =false;
        }

        if (!(args[0].equals("-en") || (args[0].equals("-de"))))
        {
            System.out.println("Warning: Wrong mode!");
            returnValue = false;
        }
        if (!new File(args[1]).exists())
        {
            System.out.println("Warning: input file does not exist!");
            returnValue = false;
        }
        /*if (this.keyKey.length() != 16)
        {
            System.out.println("Warning: input password is not valid!");
            returnValue = false;
        }*/
        return returnValue;
    }

    public static void main(String[] args)
    {
        Encrypt myEncrypt = new Encrypt();
        if (!myEncrypt.hasValidParams(args))
            return;

        EncryptCore myEC = null;
        try {
            myEC = new EncryptCore(args);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        myEC.doEncrypt();
    }

    public static String getPasswordFromCL()
    {
        Scanner sc = new Scanner(System.in);
        String inputString = sc.nextLine();
        while (inputString.length() != 16)
        {
            System.out.println("Please input 16 bit password!");
            inputString = sc.nextLine();
        }
        return inputString;
    }
}

