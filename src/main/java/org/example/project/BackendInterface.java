package org.example.project;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author yunrui huang
 * @update 04/12/2024
 * Defines the interface for the backend functionality.
 */
interface BackendInterface {
    public Data[] query(String type, Timestamp  startTime, Timestamp endTime);

    public Data[] sort(int type, Boolean isReversed);

    public Summary summary();

    public Boolean delete(int id);

    public Boolean add(String type,String title,Timestamp  time,double amount,String comment);

    public Boolean edit(int id,String type,String title,Timestamp  time,double amount,String comment);

    public Summary summaryByYear(int year);

    public Summary summaryByMonth(int year, int month);
}
