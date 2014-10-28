package pnpiot.serializationperf;

import java.io.ByteArrayOutputStream;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;

public class AvroApp {
	static String AvroSchema = "{`type`: `record`,`name`: `Car.Location`,`fields`: [{`name`: `timeStamp`,`type`: `long`},{`name`: `fixType`,`type`: `int`},{`name`: `latitude`,`type`: `int`},{`name`: `longitude`,`type`: `int`},{`name`: `heading`,`type`: `int`},{`name`: `altitude`,`type`: `int`},{`name`: `speed`,`type`: `int`}]}";

	public static void main(String[] args) throws Exception {
		AvroSchema = AvroSchema.replace('`', '"');
		long loops = 1000;
		if (args.length > 0) {
			loops = Math.abs(Integer.parseInt(args[0])) * 1000;
		}

		Schema schema = new Schema.Parser().parse(AvroSchema);
		GenericRecord locRecord = getLocRecord(schema);

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outStream, null);
		DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);

		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loops; i++) {
			for (int j = 0; j < 1000; j++) {
				datumWriter.write(locRecord, encoder);
			}
			encoder.flush();
			// byte[] serializedBytes = outStream.toByteArray();
			outStream.reset();
		}
		outStream.close();

		long elapsedTime = System.currentTimeMillis() - startTime;
		AppResult.printResult("AvroApp", loops, elapsedTime);
	}

	private static GenericRecord getLocRecord(Schema schema) {
		GenericRecord locRecord = new GenericData.Record(schema);
		locRecord.put("timeStamp", 101L);
		locRecord.put("fixType", 102);
		locRecord.put("latitude", 400);
		locRecord.put("longitude", 500);
		locRecord.put("heading", 300);
		locRecord.put("altitude", 100);
		locRecord.put("speed", 100);
		return locRecord;
	}
}

// to run the jar file in command line for 3000 loops:
// java -cp perf.jar pnpiot.serializationperf.AvroApp 3