import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by danwang on 15/7/22.
 */
public class EncryptCore
{
    private int mode;
    private File inputFile;
    private String keyKey;
    private Cipher cipher;

    public  EncryptCore(String[] args) throws UnsupportedEncodingException {
        String purpose, filePath, passWord;
        purpose = args[0];
        filePath = args[1];
        passWord = args[2];

        this.mode = (purpose.equals("-en"))?Cipher.ENCRYPT_MODE:Cipher.DECRYPT_MODE;
        this.inputFile = new File(filePath);
        this.keyKey = passWord;

        byte[] keyData = keyKey.getBytes("utf-8");
        String algorithm = "AES";
        SecretKeySpec keySpec = new SecretKeySpec(keyData, algorithm);

        try
        {
            /*
            There is no need SecretKeyFactory and SecretKey in case use "AES"
            Directly forward KeySpec to cipher.init is ok.
            */
            //SecretKeyFactory keyFactory = getInstance(algorithm);
            //SecretKey key = keyFactory.generateSecret(keySpec);

            cipher = Cipher.getInstance(algorithm);
            cipher.init(this.mode, keySpec);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e)
        {
            e.printStackTrace();
        }
    }

    private  void doEncryptCore(File fileTobeEncrypt, String targetFolder)
    {
        if (!fileTobeEncrypt.isDirectory())
        {
            try
            {
                System.out.println("Processing " + fileTobeEncrypt.getName());
                FileInputStream inputStream = new FileInputStream(fileTobeEncrypt);
                // Compose final file name
                String fileName = fileTobeEncrypt.getName();
                //String pureName = fileName.substring(0, fileName.lastIndexOf("."));
                //String suffixName = fileName.substring(fileName.lastIndexOf("."));
                //String targetFile = targetFolder+ File.separator + pureName + "_" + this.mode + suffixName;
                // there is no need to add mode in the target file name
                String targetFile = targetFolder + File.separator + fileName;
                System.out.println(targetFile);
                FileOutputStream outputStream = new FileOutputStream(targetFile);

                int blockSize = cipher.getBlockSize();
                byte[] buffer = new byte[blockSize];

                CipherInputStream cin = new CipherInputStream(inputStream, cipher);

                int lengthPerIteration;

                while ((lengthPerIteration = cin.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, lengthPerIteration);
                }

                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            File[] allFiles = fileTobeEncrypt.listFiles();
            if (allFiles.length > 0)
            {
                File tf = new File(targetFolder+File.separator+fileTobeEncrypt.getName());
                tf.mkdir();
                for (int i=0; i<allFiles.length; i++)
                {
                    doEncryptCore(allFiles[i], tf.getPath());
                }
            }
        }
    }

    public void doEncrypt()
    {
        String folderSuffix;
        if (this.mode == Cipher.ENCRYPT_MODE)
            folderSuffix = "_Encrypted";
        else
            folderSuffix = "_Decrypted";

        File targetFolder = new File(this.inputFile.getAbsoluteFile().getParent()
                                    + File.separator
                                    + this.inputFile.getName()+folderSuffix);
        targetFolder.mkdir();
        doEncryptCore(this.inputFile, targetFolder.getPath());
    }
}
