package starter.gradle;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public static String getCurrentDateString() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public static void sendTwilioSMS(String _e164From, String _e164To, String _message) {
        /* e164 format example - 14159352345 */
        Message message = Message.creator(new PhoneNumber(_e164To), new PhoneNumber(_e164From), _message).create();
        System.err.print("----------------------------------------INFO--------------------------------------------\n");
        System.err.print("\tTwilio Message Sent\n");
        System.err.print(String.format("\t_e164From=%s _e164To=%s msgsid=%s\n", _e164From, _e164To, message.getSid()));
        System.err.print(String.format("---------------------------------------------------------------------%s\n\n", Utils.getCurrentDateString()));
    }

}
