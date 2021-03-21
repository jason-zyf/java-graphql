package com.zyf.graphql.demo;

import com.zyf.graphql.vo.User;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.apache.commons.io.IOUtils;
import org.omg.CORBA.Environment;

import java.io.IOException;

/**
 * @author zyting
 * @since 2021-03-19
 */
public class GraphQLSDLDemo3 {

    public static void main(String[] args) throws IOException {

        String query = "{user(id:100){id,name,age}}";

        // 读取GraphQLw文件
        String fileName = "./graphql/user.graphqls";
        String fileContent = IOUtils.toString(GraphQLSDLDemo3.class.getClassLoader().getResource(fileName), "UTF-8");
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(fileContent);

        // 解决的是数据的查询
        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                .type("UserQuery",builder ->
                    builder.dataFetcher("user", environment -> {
                        Long id = environment.getArgument("id");
                        return new User(id, "张三：" + id, 20 + id.intValue());
                    })
                ).build();

        // 生成schema
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);

        // 根据schema对象生成
        GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema).build();

        ExecutionResult result = graphQL.execute(query);
        System.out.println(result.toSpecification());

    }

}
