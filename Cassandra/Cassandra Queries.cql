CAPTURE '~/Desktop/results/fossil_by_time_coal_0.txt'
SELECT * FROM group_project.energy_fossil_by_time WHERE (generation_fossil_hard_coal = 0) ALLOW FILTERING;
CAPTURE OFF;

CAPTURE '~/Desktop/results/renewable_by_time_wind_offshore_greater_0.txt'
SELECT * FROM group_project.energy_renewable_by_time WHERE (generation_wind_offshore > 0) ALLOW FILTERING;
CAPTURE OFF;

CAPTURE '~/Desktop/results/weather_by_time_temp_270_weather_clear.txt'
SELECT * FROM group_project.weather_by_time WHERE (temp > 270) AND (weather_main = 'clear') ALLOW FILTERING;
CAPTURE OFF;

CAPTURE '~/Desktop/results/temp_by_time_temp_greater_280_city_Valencia.txt'
SELECT * FROM group_project.temp_by_time_and_city WHERE (temp > 280) AND (city_name = 'Valencia') ALLOW FILTERING;
CAPTURE OFF;

CAPTURE '~/Desktop/results/energy_by_price_actual_price_greater_70.txt'
SELECT * FROM group_project.energy_by_price_actual WHERE (price_actual > 70) ALLOW FILTERING;
CAPTURE OFF;
