package com.jojoldu.book.springboot.service.posts;

import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsListResponseDTO;
import com.jojoldu.book.springboot.web.dto.PostsResponseDTO;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDTO;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDTO requestDTO) {
        return postsRepository.save(requestDTO.toEntity()).getId();
    }


    @Transactional
    public Long update(Long id, PostsUpdateRequestDTO requestDTO) {

        Posts posts = postsRepository.findById(id)
                                     .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        // 영속성 컨텍스트의 더티체킹을 이용해 update
        posts.update(requestDTO.getTitle(), requestDTO.getContent());

        return id;
    }

    @Transactional
    public PostsResponseDTO findById(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        return new PostsResponseDTO(posts);
    }



    /** readOnly를 넣으면,  [트랜잭션 범위는 유지]하되,  [조회 기능만 남겨]두어 속도가 개선된다.  */
    @Transactional(readOnly = true)
    public List<PostsListResponseDTO> findAllDesc() {
        return postsRepository.findAllDesc().stream()
//                .map(PostListResponseDTO::new)
                .map(posts -> new PostsListResponseDTO(posts))
                .collect(Collectors.toList());
    }


    /**
     * JpaRepository에서 이미 delete 메소드를 지원하고 있으니 이를 활용한다.
     * 엔티티를 파라미터로 삭제할 수도 있고, deleteById 메소드를 이용하면 id로도 삭제할 수 있다.
     *
     * 존재하는 Posts인지 확인을 위해 엔티티 조회 후 그대로 삭제한다.
     */
    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        postsRepository.delete(posts);
    }


}
