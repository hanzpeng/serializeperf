package pnpiot.serializationperf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;

import com.google.protobuf.InvalidProtocolBufferException;

public class AppProtobuf {
	static String AvroSchemaStr = "{`type`: `record`,`name`: `Car.Location`,`fields`: [{`name`: `timeStamp`,`type`: `long`},{`name`: `fixType`,`type`: `int`},{`name`: `latitude`,`type`: `int`},{`name`: `longitude`,`type`: `int`},{`name`: `heading`,`type`: `int`},{`name`: `altitude`,`type`: `int`},{`name`: `speed`,`type`: `int`}]}";
	static final int INNERLOOP = 2;

	public static void main(String[] args) throws Exception {
		AvroSchemaStr = AvroSchemaStr.replace('`', '"');
		long loops = 1;
		if (args.length > 0) {
			loops = Math.abs(Integer.parseInt(args[0])) * 1;
		}

		LocationProtobuf.Location.Builder builder = createLocationBuilder();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loops; i++) {
			for (int j = 0; j < INNERLOOP; j++) {
				builder.setTimeStamp(new Date().getTime());
				builder.build().writeTo(outStream);
			}
			outStream.flush();
			displaySerializedRecord(outStream);
		}
		outStream.close();

		long elapsedTime = System.currentTimeMillis() - startTime;
		Result.writeToFile("AppProtobuf", loops, elapsedTime);
	}

	private static LocationProtobuf.Location.Builder createLocationBuilder() {
		LocationProtobuf.Location.Builder builder = LocationProtobuf.Location.newBuilder();
		builder.setTimeStamp(new Date().getTime());
		builder.setFixType(111);
		builder.setLatitude(222);
		builder.setLongitude(333);
		builder.setHeading(444);
		builder.setAltitude(555);
		builder.setSpeed(666);
		return builder;
	}

	private static void displaySerializedRecord(ByteArrayOutputStream outStream) throws InvalidProtocolBufferException {
		LocationProtobuf.Location.Builder builder = LocationProtobuf.Location.newBuilder();
		
		builder.mergeFrom(outStream.toByteArray());
		LocationProtobuf.Location location = builder.build();
		System.out.println(location.getAllFields());
	}
}