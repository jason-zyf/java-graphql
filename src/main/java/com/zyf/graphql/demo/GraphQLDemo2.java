package com.zyf.graphql.demo;

import com.zyf.graphql.vo.User;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;

import static graphql.schema.GraphQLObjectType.newObject;

/**
 * 案例二：查询参数的设置
 * @author zyting
 * @since 2021-03-18
 * 下面的注释 解释了各段代码是实现 readme.txt中的哪些定义
 */
public class GraphQLDemo2 {

    public static void main(String[] args) {

        String query = "{user(id:100){id,name,age}}";

        /**
         *  type User (#定义对象)
         */
        GraphQLObjectType userObjectType = GraphQLObjectType.newObject()
                .name("User")
                .field(GraphQLFieldDefinition.newFieldDefinition().name("id").type(Scalars.GraphQLLong))
                .field(GraphQLFieldDefinition.newFieldDefinition().name("name").type(Scalars.GraphQLString))
                .field(GraphQLFieldDefinition.newFieldDefinition().name("age").type(Scalars.GraphQLInt))
                .build();

        /**
         * user: User # 指定对象以及参数类型
         */
        GraphQLFieldDefinition userFieldDefinition = GraphQLFieldDefinition.newFieldDefinition()
                .name("user")
                .type(userObjectType)
                .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLLong).build())
                .dataFetcher(environment ->{
                    Long id = environment.getArgument("id");
                    /**
                     * 查询数据库了
                     * TODO 先模拟实现
                     */
                    return new User(id, "张三:"+id, 20+id.intValue());
                })
//                .dataFetcher(new StaticDataFetcher(new User(1L, "张三", 20)))
                .build();

        /**
         * type UserQuery { #定义查询的类型}
         */
        GraphQLObjectType userQueryObjectType = newObject()
                .name("UserQuery")
                .field(userFieldDefinition)
                .build();

        /**
         *  schema {   #定义查询 }
         */
        GraphQLSchema graphQLSchema = GraphQLSchema.newSchema().query(userQueryObjectType).build();

        GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema).build();
        ExecutionResult result = graphQL.execute(query);

        System.out.println(result.toSpecification());
    }
}
