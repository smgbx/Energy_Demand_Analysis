# Spark Streaming

‘REST API to Spark DataFrame - Single Call, Save Portions.ipynb’ – This Jupyter notebook contains the original attempt to use Spark to process data received from an API call. While it did successfully retrieve data, converting the json to an rdd and then to a Spark datafile, the complex data types of the Spark dataframe made it impossible to conveniently save the data to a .csv.


‘current_weather_text’ – Folder containing output of ‘REST API…’. This contains the text version of the saved RDD.


‘current_weather_csv’- Folder containing output of ‘REST API…’. This contains the .csv version of selected columns of the Spark dataframe.


‘rel_data_csv’- Folder containing output of ‘REST API…’. This contains the .csv version of selected columns of the RDD after they have been parallelized. 


‘SparkStreaming with REST API - Save and Show DStream Results.ipynb’ – This Jupyter notebook contains the SparkStreaming work. Because the data source is an API dependent upon a discrete GET call rather than being from an object that is constantly sending data (say an IoT device), I had to first create an object to contain several calls to the API that I would then use to mimic a streaming data source. This called the API every 10 seconds in an attempt to get varying current data and stored the response into an rddQueue. I then used a DStream to read the data from the rddQueue once every second, and then processed this data by converting the JSON responses into Spark dataframes. I then printed these dataframes to the stream using .foreachRDD and saved each to a folder.


‘streaming_weather’ – Folder containing the results of the DStream processing. Each folder represents the output of one batch interval. 


‘JSON to CSV - Get New Info for Multiple Cities.ipynb’ – This Jupyter notebook is contains the finalized work using Spark to get information from an API. In this notebook, I was able to make one call to the API service per city, convert the returned json to a Pandas dataframe, and then concatenate the results in one dataframe that could be saved as a .csv file. By defining the schema of the dataframe, expanding the components of the nested JSON, and making new columns for nested elements, I was able to produce help, relevant information that could then be added to our existing dataset. 


‘new_weather_data_5_cities’ - .csv file containing current weather data for all five cities in Spain. 


## Shelby Increment Notes

Using Spark and SparkStreaming, my goal was to find a way to incorporate new data into our existing dataset. To do this, I found the same API service that was used to populate the data of our original dataset. Unfortunately, due to a paywall, I was only able to use the service that provided information about the current weather rather than historical weather, which would have made querying the data interesting. However, I was still able to make it work using this API. There are two primary ways I did this: using Spark, and using a mock-version of SparkStreaming. 


![](https://github.com/smgbx/Energy_Demand_Analysis/blob/master/Spark%20Streaming/weather_5_cities_screenshot.png)
