# Cassandra

‘Cassandra Tables Creation.cql’ – This file contains the script that was used to create and load data into  five different Cassandra tables. Because the data is just text, the class used was SimpleStrategy. A replication factor of 3 was arbitrarily decided upon.


‘Cassandra Queries.cql’ – This file contains the queries used for each table. The result of the queries was stored into a unique txt file.


‘Cassandra Results” – This folder contains the results of the five .cql queries used for each of the Cassandra tables, as well as screenshots of the successfully created tables. 

## Shelby Increment Notes

Because joins aren’t possible in Cassandra, it was necessary to keep the two tables separate. Furthermore, since Cassandra operates by a query-first approach, I created several tables within Cassandra such that each table was designed for a specific query. Though it did result it duplication of data, this design is good for high-load queries that usually happened in big data. The insights gleaned from these queries seemed rather unhelpful compared to the query capabilities of HQL and mySQL. Whereas HQL/mySQL can perform direct analysis on the data (such as calculating averages, join functions, etc.), it seems like there would have to be some secondary analysis step performed with any data returned from a Cassandra query.






















