package com.netflix.spring_lolomo_service.datafetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.spring_lolomo_service.codegen.types.Show;
import com.netflix.spring_lolomo_service.codegen.types.ShowCategory;

import java.util.List;

@DgsComponent
public class LolomoDataFetcher {

    // GraphiQL: http://localhost:8080/graphiql?path=/graphql
    @DgsQuery
    public List<ShowCategory> lolomo() {
        return List.of(ShowCategory.newBuilder()
                .name("Top 10")
                .shows(List.of(
                        Show.newBuilder()
                                .title("Show 1").build())).build());
    }
}
