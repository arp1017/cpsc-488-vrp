package Zeus;

import java.io.*;


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems</p>
 * <p>Description: </p>
 * <p>Copyright:(c) 2001-2003</p>
 * <p>Company: </p>
 * @author Sam R. Thangiah
 * @version 1.0
 */
public class GeneralZeusTools {
    /**
 * This method will save (serialize) the passed instance (Object O) to the file
 * specified in file variable. Whatever object serialized this must implement
 * this serializable interface. Also all objects referenced within this object must
 * also implement the serializable interface.
 *
 * @param O Any instance of any class casted into Object
 * @param file The file to save the object into
 */
    public static void object_Serialize(Object O, File file) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            try {
                // write the instance of the object into the the ObjectOutputStream
                out.writeObject(O);
                out.flush();
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                out.close();
                fileOut.close();
            }
        } catch (IOException IOE) {
            System.err.println(IOE);
        }
    }

    /**
 * This method will extract (deserialize) an instance of an object that serialized (saved)
 * into file. After extract it will upcast the instance into type Object and return it
 * as an Object. All objects that are deserialized this way must implement the serializable
 * interface.
 *
 * @param file The file to read the object from
 * @return the Object that was read in.
 */
    public static Object object_Deserialize(File file) {
        try {
            Object O = null;
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream input = new ObjectInputStream(fileIn);

            try {
                // read the instance that is in the file and upcast it into an object.
                O = (Object) input.readObject();
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                input.close();
                fileIn.close();

                return O;
            }
        } catch (IOException IOE) {
            System.err.println(IOE);
        }

        return null;
    }

    /**
 * This method will create a clone of any object passed into it, and return a handle
 * to the clone as an object.
 *
 * @param O Object that needs to be cloned.
 * @param tempFileLocation Temporary Folder to use for making the clone.
 * @return clone of the object.
 */
    public static Object object_Clone(Object O, String tempFileLocation) {
        Object newO = null;

        try {
            // create temporary file to serialize object into
            File f = File.createTempFile("cln", null, new File(tempFileLocation));

            // serialize the object into temp file
            object_Serialize(O, f);

            // deserialize the object from the temp file into a new handle (this will create a clone)
            newO = object_Deserialize(f);

            // delete temp file.
            f.delete();
        } catch (Exception e) {
            System.err.println(e);
        }

        return newO;
    }
}
