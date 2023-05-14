package com.hanamja.moa.api.service.post;

import com.hanamja.moa.api.dto.post.request.CreatePostRequestDto;
import com.hanamja.moa.api.dto.post.request.EditPostRequestDto;
import com.hanamja.moa.api.dto.post.response.CreatePostResponseDto;
import com.hanamja.moa.api.dto.post.response.PostDetailInfoResponseDto;
import com.hanamja.moa.api.dto.post.response.PostImageResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.board_category.BoardCategory;
import com.hanamja.moa.api.entity.post.Post;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

    /* resolve 함수 - 객체의 id를 validate 한 뒤 resolve 해오는 내부 함수들 */
    BoardCategory resolveBoardCategoryById(Long boardCategoryId);

    Post resolvePostById(Long postId);

    Post resolvePostByIdAndUserId(Long postId, Long userId);

    /* Image 등록 메서드 */
    String registerImageToPostImage(MultipartFile image);

    /* Controller 에서 사용할 함수들 */
    DataResponseDto<PostDetailInfoResponseDto> getPostDetailInfo(UserAccount userAccount, Long postId);

    CreatePostResponseDto createNewPost(UserAccount userAccount, CreatePostRequestDto createPostRequestDto);

    PostImageResponseDto uploadImage(UserAccount userAccount, MultipartFile image);

    CreatePostResponseDto editPost(UserAccount userAccount, EditPostRequestDto editPostRequestDto);

    void deleteBoardPost(Long postId, UserAccount userAccount);

    void likeBoardPost(Long postId, UserAccount userAccount);

    void bookmarkBoardPost(Long postId, UserAccount userAccount);

}
