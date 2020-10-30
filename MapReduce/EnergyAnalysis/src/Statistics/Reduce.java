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
			
			int count = 0;
			for (FloatWritable var : value) {
				Float tmp = var.get();
				if(tmp < min)
					min = tmp;
				if(tmp > max)
					max = tmp;
				
				total += tmp;
				System.out.print(count);
				count++;
			}
			
			Float avg = total / count;
			String out = String.format("total: %10.2f, min: %5.2f, max: %5.2f, avg: %5.2f,", total, min, max, avg);
			
			System.out.print(out);
			context.write(key, new Text(out));

	}
}
