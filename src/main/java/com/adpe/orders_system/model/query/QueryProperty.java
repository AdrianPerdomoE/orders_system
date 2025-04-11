package com.adpe.orders_system.model.query;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class QueryProperty {
   
    private String field;
    private Operator operator; // 
    private Object value; // Can hold either a single value or a list of values


    public void setOperator(String operator) {
        this.operator =Operator.fromString(operator);
    }

    public String getOperator() {
        return operator.getString();
    }
    
}

