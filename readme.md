# CS622 Project



## 03/13/2024

### Project Function

1. Create
   1. Add new data to DB
2. Delete
   1. Delete data via id
   2. Delete all data
3. Update
   1. update data via id
   2. allow to update {title, time, amount, comment}
   3. No allow{type}
4. Query
   1. query via type
   2. query via time
   3. query all sort by time
5. Summary
   1. via month
   2. via week
   3. via year
   4. to today
6. Export/Import
   1. export to excel
   2. import from excel



## Class and Interface

### Class:

```java
Data{
	int ID;
	String type;
	String title;
	Date time;
    Float amount;
	String comment
}

Summary{
	Float in;
	Float out;
	Float total;
}


```





### Functions:

```pseudocode

query(string type, date startTime, date endTime):Data[]

summary():Summary

delete(int id):Boolean

add(String type,String title,Date time,Float amount,String comment):Boolean

edit(int id,String type,String title,Date time,Float amount,String comment):Boolean

summaryByYear(int year):Summary

summaryByMonth(int month):Summary
```







