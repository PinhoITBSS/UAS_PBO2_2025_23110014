/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.mycompany.mavenproject4.VisitLog;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

public class GraphQLConfig {
   public static GraphQL init() throws IOException {
       InputStream schemaStream = GraphQLConfig.class.getClassLoader().getResourceAsStream("schema.graphqls");
       
       if (schemaStream == null) {
           throw new RuntimeException("schema.graphqls not found in classpath.");
       }

       String schema = new String(Objects.requireNonNull(schemaStream).readAllBytes());

         TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schema);
         RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> builder
                          .dataFetcher("visitLogs", env -> VisitLog.getAllVisitLogs())
                          .dataFetcher("visitLogById", env -> {
                              int id = env.getArgument("id");
                              return VisitLog.getVisitLogById(id);
                          })
                          )
                .type("Mutation", builder -> builder
                          .dataFetcher("addVisitLog", env -> VisitLog.add(
                            String studentName = env.getArgument("studentName");
                            String studentId = env.getArgument("studentId");
                            String studyProgram = env.getArgument("studyProgram");
                            String purpose = env.getArgument("purpose");
                            return VisitLog.addVisitLog(studentName, studentId, studyProgram, purpose);
                          ))
                          .dataFetcher("deleteVisitLog", env -> VisitLog.delete(
                            int id = env.getArgument("id");
                            return VisitLog.deleteVisitLog(id);
                          ))
                          .dataFetcher("updateVisitLog", env -> VisitLog.update{
                            int id = env.getArgument("id");
                            String studentName = env.getArgument("studentName");
                            String studentId = env.getArgument("studentId");
                            String studyProgram = env.getArgument("studyProgram");
                            String purpose = env.getArgument("purpose");
                            return VisitLog.updateVisitLog(id, studentName, studentId, studyProgram, purpose);
                          })
                          )
                .build();
                
            GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
            return GraphQL.newGraphQL(graphQLSchema).build();
   }
}