package Statistics;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


public class Reduce
  extends Reducer<Text, FloatWritable, Text, Text> {
	@Override
	public void reduce(Text key, Iterable<FloatWritable> value, Context context)
			throws IOException, InterruptedException {

//			if(key.toString() != "total_load_actual")
//				return;
			Float total = (float) 0;
			Float min = Float.POSITIVE_INFINITY;
			Float max = Float.NEGATIVE_INFINITY;
			Float mean = (float) 0;
			Float dSquared = (float) 0;
			
			int count = 0;
			for (FloatWritable var : value) {
				System.out.print(count);
				count++;
				
				Float tmp = var.get();
				
				Float meanDifferential = (tmp - mean) / count;
				
				Float newMean = mean + meanDifferential;

				Float dSquaredIncrement = (tmp - newMean) * (tmp - mean);

				Float newDSquared = dSquared + dSquaredIncrement;

				mean = newMean;

				dSquared = newDSquared;
				
				if(tmp < min)
					min = tmp;
				if(tmp > max)
					max = tmp;
				
				total += tmp;
				
			}
			Float populationVariance = dSquared / count;
			
			Double populationStdev = Math.sqrt(populationVariance);
			
			Float sampleVariance = dSquared / (count - 1);
			
			Double sampleStdev = Math.sqrt(sampleVariance);
			
			Float avg = total / count;
			String out = String.format("total: %10.2f, min: %5.2f, max: %5.2f, avg: %5.2f,", total, min, max, avg);
			String outstd = String.format("APPROXIMATIONS |Pop Var: %5.2f, Pop STDEV: %5.2f, Samp Var: %5.2f, Samp STDEV: %5.2f,", populationVariance, populationStdev, sampleVariance, sampleStdev);

			System.out.print(out);
			context.write(key, new Text(out));

			System.out.print(outstd);
			context.write(key, new Text(outstd));
			
	}
}
