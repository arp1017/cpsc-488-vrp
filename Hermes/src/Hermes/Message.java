package Hermes;

import java.util.LinkedList;


/**
 *
 * <p>Title: Message.java </p>
 * <p>Description: This class provides XML messaging without exposing the user to
 *                any of the XML details.</p>
 * @author Adrian Matlack
 * @version 2.3
 */
public class Message {
    private String messageType = ""; //Message type.
    private LinkedList argumentList = new LinkedList(); //List of arguments.

    /**
    * Empty constructor.
    */
    public Message() {
    }

    /**
    * Constructor to build a Message object from an XML character string.
    * @param message  XML string message
    * @throws Exception
    */
    public Message(String message) throws Exception {
        setMessage(message);
    }

    /**
    * Constructor to build a Message object from another Message object.
    * @param message  Message object
    */
    public Message(Message message) {
        messageType = message.messageType;
        argumentList = (LinkedList) message.argumentList.clone();
    }

    /**
    * this method Adds an argument to the message.
    * @param identifier  identifies the argument type
    * @param value  value of the argument
    */
    public void addArgument(String identifier, String value) {
        argumentList.add(new Argument(identifier, value));
    }

    /**
    * Overloaded method. It Adds a nested message as an argument to the message.
    * @param nestedMessage  message to be added as an argument
    */
    public void addArgument(Message nestedMessage) {
        argumentList.add(new Argument(nestedMessage));
    }

    /**
    * Clears all of the arguments from the message.
    */
    public void clearArguments() {
        messageType = "";
        argumentList.clear();
    }

    /**
    * Returns the first instance of the nested message indicated by 'identifier'.
    * @param identifier  identifies the argument type
    * @return Message  nested message
    */
    public Message getMessage(String identifier) {
        return getMessage(identifier, 1);
    }

    /**
    * Returns the 'repititionNumber'th instance of the nested message indicated by 'identifier'.
    * @param identifier  identifies the argument type
    * @param repititionNumber  nth instance of the argument
    * @return Message  nested message
    */
    public Message getMessage(String identifier, int repititionNumber) {
        boolean continueLoop = true;
        int timesFound = 0;
        Argument arg;
        Message tempMessage = null;

        for (int i = 0; (i < argumentList.size()) && continueLoop; i++) //Loop through arguments.
         {
            arg = (Argument) argumentList.get(i);

            if (arg.getIdentifier().equals(identifier)) //If the argument is of type 'identifier'.
             {
                timesFound++; //Increment times found.

                if (timesFound == repititionNumber) //If times found is equal to the repitition number.
                 {
                    tempMessage = arg.getNestedMessage(); //Get the nested message.
                    continueLoop = false; //Terminate loop.
                }
            }
        }

        return tempMessage;
    }

    /**
    * Returns the string composed of the XML tags and information of the message.
    * @return String  XML message in String type
    */
    public String getMessageString() {
        String message = "";
        Argument arg;

        message = '<' + messageType + '>'; //Set opening tag.

        for (int i = 0; i < argumentList.size(); i++) //Loop through all arguments.
         {
            arg = (Argument) argumentList.get(i);

            if (arg.hasNestedMessage()) { //If the argument has a nested message
                message += arg.getNestedMessage().getMessageString(); //Then add the string of that message.
            } else { //Otherwise add the tags and value of the argument.
                message += ('<' + arg.getIdentifier() + '>' + arg.getValue() +
                "</" + arg.getIdentifier() + '>');
            }
        }

        message += ("</" + messageType + '>'); //Set closing tag.

        return message;
    }

    /**
    * Returns the type of the message.
    * @return String  type of the message
    */
    public String getMessageType() {
        return messageType;
    }

    /**
    * Returns value of the 'repititionNumber'th instance of the argument indicated by 'identifier'.
    * @param identifier  identifies the argument type
    * @param repititionNumber  nth instance of the argument
    * @return String  value of the argument
    */
    public String getValue(String identifier, int repititionNumber) {
        boolean continueLoop = true;
        int timesFound = 0;
        String value = "";
        Argument arg;

        for (int i = 0; (i < argumentList.size()) && continueLoop; i++) //Loop through all arguments.
         {
            arg = (Argument) argumentList.get(i);

            if (arg.getIdentifier().equals(identifier)) //If the argument is of type 'identifier'.
             {
                timesFound++; //Increment times found.

                if (timesFound == repititionNumber) //If times found is equal to 'repititionNumber'.
                 {
                    value = arg.getValue(); //Get the value
                    continueLoop = false; //Terminate loop.
                }
            }
        }

        return value;
    }

    /**
    * Returns value of the first instance of the argument indicated by 'identifier'.
    * @param identifier  identifies the argument
    * @return String  value of the argument
    */
    public String getValue(String identifier) {
        return getValue(identifier, 1);
    }

    /**
    * Sets the message to the XML character string 'message'.
    * @param message  XML string
    * @throws Exception
    */
    public void setMessage(String message) throws Exception {
        String identifier;
        String value;

        message = message.trim();

        if (message.charAt(0) == '<') //If the first character is an opening bracket.
         {
            messageType = message.substring(1, message.indexOf('>', 1)).trim(); //Set message type to the opening tag identifier.
            message = message.substring(message.indexOf('>', 1) + 1).trim(); //Set message string to everything after opening tag.
            identifier = message.substring(message.lastIndexOf("</") + 2,
                    message.length() - 1).trim(); //Get identifier of the closing tag of the message.

            if (!identifier.equals(messageType)) { //If the identifiers of the opening and closing tags are not the same
                throw new Exception(); //Throw an exception.
            }

            message = message.substring(0, message.lastIndexOf("</")).trim(); //Set message string to everything before the last closing tag.

            while (message.length() > 0) //While the message string is not zero length.
             {
                if (message.charAt(0) != '<') { //If the first character is notan opening bracket
                    throw new Exception(); //Throw an exception.
                }

                identifier = message.substring(1, message.indexOf('>', 1)).trim(); //Get identifier of opening tag.
                message = message.substring(message.indexOf('>', 1) + 1).trim(); //Set message string to everything after opening tag.
                value = message.substring(0,
                        message.indexOf("</" + identifier + '>')); //Set value to be everything before closing tag specified by identifier.
                message = message.substring(message.indexOf("</" + identifier +
                            '>') + identifier.length() + 3).trim(); //Set message string to be everything after closing tag specified by identifier.

                if ((value.length() == 0) || (value.charAt(0) != '<')) { //If the argument is not a nested message.
                    argumentList.add(new Argument(identifier, value)); //Add the argument to the argument list.
                } else {
                    argumentList.add(new Argument(
                            new Message('<' + identifier + '>' + value + "</" +
                                identifier + '>'))); //Create a new nested message and add it to the argument list.
                }
            }
        } else {
            throw new Exception();
        }
    }

    /**
    * Sets the type of the message.
    * @param type  type of message
    */
    public void setMessageType(String type) {
        messageType = type;
    }

    /**
    * This method return true if the messages being compared are equal.. Added by Ola
    * @param msg the message to be tested for equality
    * @return boolean weither messages are equal or not
    */
    public boolean equals(Message msg) {
        if ((this.getMessageType()).equals((msg.getMessageType()))) { //check if types are the same using string .equals

            if (this.getMessageString().equals(msg.getMessageString())) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    /**
    * This will return a string representation of the Message
    * @return message string
    */
    public String toString() {
        return getMessageString();
    }

    /**
    * <p>Title: Argument.java</p>
    * <p>Description: The argument class is a class that gives the flexibility
    *                 passing more than one agurment inside one argument
    *                 wrapper class</p>
    * @author Adrian Matlack
    * @version 2.3
    */
    private class Argument {
        boolean hasNestedMessage; //Indicates if the argument is a nested message.
        String identifier; //String used to identify the argument.
        String value; //Value of the argument if it is not a nested message.
        Message nestedMessage; //Nested message (if there is one).

        /**
        * Constructor used to initialize an argument that has a specific value.
        * @param identifier  identifies the argument
        * @param value  value of the argument
        */
        public Argument(String identifier, String value) {
            hasNestedMessage = false;
            this.identifier = identifier;
            this.value = value;
        }

        /**
        * Constructor used to initialize an argument that has a nested message.
        * @param message  nested message as an argument
        */
        public Argument(Message message) {
            hasNestedMessage = true;
            identifier = message.getMessageType();
            nestedMessage = new Message(message);
        }

        /**
        * Returns the identifier of the argument.
        * @return String  identifier of the argument
        */
        public String getIdentifier() {
            return identifier;
        }

        /**
        * Returns the value of the argument.
        * @return String  value of the argument
        */
        public String getValue() {
            return value;
        }

        /**
        * Returns the nested message of the argument.
        * @return Message  nested message
        */
        public Message getNestedMessage() {
            return new Message(nestedMessage);
        }

        /**
        * Returns true if the argument has a nested message.
        * @return boolean  state of nested message within argument
        */
        public boolean hasNestedMessage() {
            return hasNestedMessage;
        }
    }
}
