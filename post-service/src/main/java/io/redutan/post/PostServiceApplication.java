package io.redutan.post;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Arrays;


@SpringBootApplication
public class PostServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostServiceApplication.class, args);
    }
}

@Component
class SamplePostClr implements CommandLineRunner {
    private final PostRepository postRepository;

    @Autowired
    SamplePostClr(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        postRepository.save(
                Arrays.asList(
                        new Post(null, "유튜브", "유튜브 비디오 "),
                        new Post(null, "페이스북", "페이스북 글")
                )
        );
        postRepository.findAll().forEach(System.out::println);
    }
}

@RefreshScope
@RestController
class MessageRestController {
    private final String message;

    @Autowired
    MessageRestController(@Value("${message}") String message) {
        this.message = message;
    }

    @GetMapping("/message")
    String message() {
        return message;
    }
}

@Entity
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)    // JPA
@AllArgsConstructor
class Post {
    @Id
    @GeneratedValue
    private Long postId;
    private String title;
    @Lob
    private String content;
}

@RepositoryRestResource
interface PostRepository extends JpaRepository<Post, Long> {
}