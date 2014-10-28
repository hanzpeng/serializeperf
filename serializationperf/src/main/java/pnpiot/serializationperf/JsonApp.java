package pnpiot.serializationperf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import pnpiot.serializationperf.Location;

public class JsonApp {
	static String jsonInputStr = "{\"timeStamp\":101,\"fixType\":102,\"latitude\":400,\"longitude\":500,\"heading\":300,\"altitude\":100,\"speed\":255}";

	public static void main(String[] args) throws Exception {
		long loops = 1000;
		if (args.length > 0) {
			loops = Math.abs(Integer.parseInt(args[0])) * 1000;
		}
		ObjectMapper mapper = new ObjectMapper();
		Location jsonObject = mapper.readValue(jsonInputStr, Location.class);
		long startTime = System.currentTimeMillis();
		String jsonString = null;
		for (int i = 0; i < loops; i++) {
			for (int j = 0; j < 1000; j++) {
				jsonObject.setTimeStamp(new Date().getTime());
				jsonString = mapper.writeValueAsString(jsonObject);
			}
			System.out.println(new Date((Long) jsonObject.getTimeStamp()) + "  " + jsonString);
		}
		long elapsedTime = System.currentTimeMillis() - startTime;
		Result.writeToFile("JsonApp", loops, elapsedTime);
	}
}

// To generate the jar files with dependencies
// In Eclipse, File->Export->Java->Runnable Jar File
// for Launch configuration: select JsonApp - serializationperf
// Set destination as serializationperf\perf.jar

// To run the jar files in Command Prompt
// Start Command Prompt as admin
// java -jar jsonjackson.jar 20
// open jsonjackson.txt in the current working folder

// To run the jar files in PowerShell
// Start PowerShell as admin
// &"C:\Program Files\Java\jdk1.8.0_05\bin\java.exe" -jar jsonjackson.jar 20
// open jsonjackson.txt in the current working folder

// To use measure-command
// Start PowerShell as admin
// measure-command { &"C:\Program Files\Java\jdk1.8.0_05\bin\java.exe" -jar jsonjackson.jar 20}
// open jsonjackson.txt in the current working folder

// to run the jar file in command line for 3000 loops:
// java -cp perf.jar pnpiot.serializationperf.JsonApp 3

