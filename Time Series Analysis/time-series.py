import csv
from pyspark.sql import SparkSession
import pyspark.sql.functions as f
# User Defined Functions
from pyspark.sql.functions import udf
from pyspark.sql.types import DoubleType
# Stats
from pyspark.ml.feature import VectorAssembler
from pyspark.ml.stat import Correlation
from math import sqrt

import numpy as np
import pandas as pd

import matplotlib.pyplot as plt

import statsmodels.api as sm
import itertools

# Creates a new Spark session w/in Python
spark = SparkSession.builder.appName("Final Project").getOrCreate()

# Reads the local csv stored on my computer, it does have a header
energy_df = spark.read.csv("C:\\Users\\Wes\\Desktop\\CS 490\\energy_dataset.csv", header = True, inferSchema=True)
weather_df = spark.read.csv("C:\\Users\\Wes\\Desktop\\CS 490\\weather_features.csv", header = True, inferSchema=True)

joined_df = energy_df.join(weather_df, energy_df.time == weather_df.dt_iso)

# Convert Kelvin temps to Farhenheight
k_to_f_udf = udf(lambda kelvin: (float(kelvin) - 273.15) * (9/5) + 32, DoubleType())
joined_df = joined_df.withColumn("temp_f", k_to_f_udf(joined_df.temp))
joined_df = joined_df.withColumn("temp_min_f", k_to_f_udf(joined_df.temp_min))
joined_df = joined_df.withColumn("temp_max_f", k_to_f_udf(joined_df.temp_max))
joined_df.na.fill(0)

# Getting two columns from the dataset, converting to a Pandas Style dataframe
history_df = joined_df.select(["time", "price_actual"]).toPandas()

# Converting from object type to datetime
history_df['time'] = pd.to_datetime(history_df['time'])

# Grouping by time and taking the sum of price_actual
history_df = history_df.groupby('time')['price_actual'].sum().reset_index()

# Setting the correct index for DF
history_df = history_df.set_index('time')
print(history_df.index)

# Resampling data to be on a monthly basis, taking the mean of price_actual
y = history_df["price_actual"].resample("MS").mean()
y.plot(figsize=(15, 6))
plt.show()

# Decomposition
# Trend - Shows the general 'trending' growth of the data
# Seasonal - Shows the pattern of the data within seasons
# Residuals - The closer the value is to 0 the more predictable that data is during that time period
#   The farther from 0 the more unexpected that trend was
from pylab import rcParams
rcParams['figure.figsize'] = 18, 8
decomposition = sm.tsa.seasonal_decompose(y, model='additive')
fig = decomposition.plot()
plt.show()

# ARIMA Models
# The goal of ARIMA is get a low AIC score, the commented out code tests different ARIMA settings to find the lowest
# AIC score. That settings for the lowest score was used for the model
"""
p = d = q = range(0, 2)
pdq = list(itertools.product(p, d, q))
seasonal_pdq = [(x[0], x[1], x[2], 12) for x in list(itertools.product(p, d, q))]
print('Examples of parameter combinations for Seasonal ARIMA...')
print('SARIMAX: {} x {}'.format(pdq[1], seasonal_pdq[1]))
print('SARIMAX: {} x {}'.format(pdq[1], seasonal_pdq[2]))
print('SARIMAX: {} x {}'.format(pdq[2], seasonal_pdq[3]))
print('SARIMAX: {} x {}'.format(pdq[2], seasonal_pdq[4]))

for param in pdq:
    for param_seasonal in seasonal_pdq:
        try:
            mod = sm.tsa.statespace.SARIMAX(y,
                                            order=param,
                                            seasonal_order=param_seasonal,
                                            enforce_stationarity=False,
                                            enforce_invertibility=False)
        except:
            continue

results = mod.fit()
print('ARIMA{}x{}12 - AIC:{}'.format(param, param_seasonal, results.aic))
"""

# ARIMA model creation and summary stats
mod = sm.tsa.statespace.SARIMAX(y,
                                order=(1, 1, 1),
                                seasonal_order=(1, 1, 1, 12),
                                enforce_stationarity=True,
                                enforce_invertibility=True)
results = mod.fit()
print(results.summary().tables[1])
results.plot_diagnostics(figsize=(16, 8))
plt.show()

# One-step ahead forecast graph, trying to predict the next data point using the previosu data point
pred = results.get_prediction(start=pd.to_datetime('2018-01-01'), dynamic=False)
pred_ci = pred.conf_int()
ax = y['2015':].plot(label='observed')
pred.predicted_mean.plot(ax=ax, label='One-step ahead Forecast', alpha=.7, figsize=(14, 7))
ax.fill_between(pred_ci.index,
                pred_ci.iloc[:, 0],
                pred_ci.iloc[:, 1], color='k', alpha=.2)
ax.set_xlabel('Date')
ax.set_ylabel('Price Actual')
plt.legend()
plt.show()

# Trying to predict the next few years given our current data
pred_uc = results.get_forecast(steps=20)
pred_ci = pred_uc.conf_int()
ax = y.plot(label='observed', figsize=(14, 7))
pred_uc.predicted_mean.plot(ax=ax, label='Forecast')
ax.fill_between(pred_ci.index,
                pred_ci.iloc[:, 0],
                pred_ci.iloc[:, 1], color='k', alpha=.25)
ax.set_xlabel('Date')
ax.set_ylabel('Price Actual')
plt.legend()
plt.show()
