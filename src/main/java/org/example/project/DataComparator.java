package org.example.project;

import java.util.Comparator;
import java.util.Date;

/**
 * @author yunrui huang
 * @update 04/12/2024
 * Comparator for sorting Data objects based on various criteria.
 */
public class DataComparator implements Comparator<Data> {
    private int type;
    private Boolean isReversed;

    /**
     * Init the comparator of the Data, using that from sort method in array
     * @param type
     * there are 6 choose to compare two data
     * 0:id, 1:type, 2:title, 3:time, 4:amount, 5:comment
     * @param isReversed
     * set TRUE to sort as decrease, set FALSE to sort as increase(default)
     */
    public DataComparator(int type, Boolean isReversed) {
        this.type = type;
        if(this.type > 5 || this.type < 0){
            this.type = 0;
        }
        this.isReversed = isReversed;
    }

    /**
     * Compares two Data objects based on the specified criteria.
     * @param o1
     * The first Data object to be compared.
     * @param o2
     * The second Data object to be compared.
     * @return
     * A negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     */
    @Override
    public int compare(Data o1, Data o2) {
        int result = 0;

        //switch to the case the user want to compare to
        switch (this.type){
            case 0:
                if(o1.getId() < o2.getId()){
                    result = -1;
                }else if(o1.getId() == o2.getId()){
                    result = 0;
                }else{
                    result = 1;
                }
                break;
            case 1:
                result = o1.getType().compareTo(o2.getType());
                break;
            case 2:
                result = o1.getTitle().compareTo(o2.getTitle());
                break;
            case 3:
                result = o1.getTime().compareTo(o2.getTime());
                break;
            case 4:
                if(o1.getAmount() < o2.getAmount()){
                    result = -1;
                }else if(o1.getAmount() == o2.getAmount()){
                    result = 0;
                }else{
                    result = 1;
                }
                break;
            case 5:
                result = o1.getComment().compareTo(o2.getComment());
                break;
            default:
                result = 0;
        }

        // check if reversed, reversed the result
        if(this.isReversed){
            if(result == -1){
                result = 1;
            }else if(result == 1){
                result = -1;
            }else{
                result = 0;
            }
        }

        return result;
    }
}
