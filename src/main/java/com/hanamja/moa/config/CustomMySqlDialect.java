package com.hanamja.moa.config;

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomMySqlDialect extends MySQL8Dialect {
    public CustomMySqlDialect() {
        super();
        registerFunction("GROUP_CONCAT", new StandardSQLFunction("GROUP_CONCAT", StandardBasicTypes.STRING));
    }
}
