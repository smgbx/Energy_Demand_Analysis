package Statistics;

import java.util.*;

//import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Map
  extends Mapper<LongWritable, Text, Text, FloatWritable> {
	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
			HashMap<Integer, String> energyHeaderMap = new HashMap<Integer, String>(); 
			
			energyHeaderMap.put(0, "time");
			energyHeaderMap.put(1, "generation_biomass");
			energyHeaderMap.put(2, "generation_fossil_brown_coal_lignite");
			energyHeaderMap.put(3, "generation_fossil_coal_derived_gas");
			energyHeaderMap.put(4, "generation_fossil_gas");
			energyHeaderMap.put(5, "generation_fossil_hard_coal");
			energyHeaderMap.put(6, "generation_fossil_oil");
			energyHeaderMap.put(7, "generation_fossil_oil_shale");
			energyHeaderMap.put(8, "generation_fossil_peat");
			energyHeaderMap.put(9, "generation_geothermal");
			energyHeaderMap.put(10, "generation_hydro_pumped_storage_aggregated");
			energyHeaderMap.put(11, "generation_hydro_pumped_storage_consumption");
			energyHeaderMap.put(12, "generation_hydro_run_of_river_and_poundage");
			energyHeaderMap.put(13, "generation_hydro_water_reservoir");
			energyHeaderMap.put(14, "generation_marine");
			energyHeaderMap.put(15, "generation_nuclear");
			energyHeaderMap.put(16, "generation_other");
			energyHeaderMap.put(17, "generation_other_renewable");
			energyHeaderMap.put(18, "generation_solar");
			energyHeaderMap.put(19, "generation_waste");
			energyHeaderMap.put(20, "generation_wind_offshore");
			energyHeaderMap.put(21, "generation_wind_onshore");
			energyHeaderMap.put(22, "forecast_solar_day_ahead");
			energyHeaderMap.put(23, "forecast_wind_offshore_eday_ahead");
			energyHeaderMap.put(24, "forecast_wind_onshore_day_ahead");
			energyHeaderMap.put(25, "total_load_forecast");
			energyHeaderMap.put(26, "total_load_actual");
			energyHeaderMap.put(27, "price_day_ahead");
			energyHeaderMap.put(28, "price_actual");
			
			String line = value.toString();
		
        	// if header skip
            if (key.get() == 0 && line.contains("time"))
                return;
            else {
            	String[] columns = line.split(",");
            	for (int i = 1; i < columns.length; i++) {
            	    if(i == 0)
            	    	continue;
            		if(!columns[i].isEmpty()) {
            	    	context.write(new Text(energyHeaderMap.get(i)), new FloatWritable(Float.parseFloat(columns[i])));
            	    }
            	}
            }	
	}
}
