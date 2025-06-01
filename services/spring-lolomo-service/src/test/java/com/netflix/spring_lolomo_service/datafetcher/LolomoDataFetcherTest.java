package com.netflix.spring_lolomo_service.datafetcher;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.test.EnableDgsTest;
import com.netflix.spring_lolomo_service.repository.ShowsRepository;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = {LolomoDataFetcher.class, ShowsRepository.class})
@EnableDgsTest
class LolomoDataFetcherTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Test
    void search() {
        @Language("GraphQL")
        var query = """
            {
                search(filter: {title: "The"}) {title}
            }
        """;

        List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(query, "data.search[+].title");
        assert titles.size() == 1;
    }
}