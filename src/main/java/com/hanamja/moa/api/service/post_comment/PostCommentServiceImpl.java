package com.hanamja.moa.api.service.post_comment;

import com.hanamja.moa.api.dto.post_comment.request.CreatePostCommentRequestDto;
import com.hanamja.moa.api.dto.post_comment.request.ModifyPostCommentRequestDto;
import com.hanamja.moa.api.dto.post_comment.response.PostCommentResponseDto;
import com.hanamja.moa.api.entity.notification.Notification;
import com.hanamja.moa.api.entity.notification.NotificationRepository;
import com.hanamja.moa.api.entity.post.Post;
import com.hanamja.moa.api.entity.post.PostRepository;
import com.hanamja.moa.api.entity.post_comment.PostComment;
import com.hanamja.moa.api.entity.post_comment.PostCommentRepository;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public PostCommentResponseDto createPostComment(Long postId, Long userId, Long departmentId, CreatePostCommentRequestDto requestDto) {
        PostComment parentPostComment = null;

        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException(
                HttpStatus.NOT_FOUND,
                "존재하지 않는 게시글입니다.")
        );

        User writer = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                HttpStatus.NOT_FOUND,
                "존재하지 않는 유저입니다.")
        );

        if (requestDto.getParentCommentId() != null) {
            parentPostComment = postCommentRepository.findById(requestDto.getParentCommentId()).orElseThrow(() -> new NotFoundException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 댓글입니다.")
            );
        }

        // TODO 추후 공통 로직으로 변경
        Long commentDepartmentId = post.getBoardCategory().getBoard().getDepartment().getId();
        if (departmentId != commentDepartmentId) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "작성 권한이 없는 게시판입니다.");
        }

        PostComment postComment = PostComment.builder()
                .post(post)
                .parentComment(parentPostComment)
                .user(writer)
                .content(requestDto.getContent())
                .isReply(parentPostComment != null)
                .commentOrder(requestDto.getCommentOrder())
                .build();

        postComment = postCommentRepository.save(postComment);

        // TODO 임시로 메세지 내용 채워놓음 논의 후 변경 필요
        notificationRepository.save(
                Notification.builder()
                        .sender(writer)
                        .receiver(post.getUser())
                        .content("[ " + post.getTitle() + " ] " + post.getTitle() + " 게시글에 댓글이 달렸어요. 우리 한번 확인해볼까요?")
                        .reason("게시글 생성자: " + post.getUser().getName() + "님")
                        .build()
        );

        // TODO 임시로 메세지 내용 채워놓음 논의 후 변경 필요
        if (parentPostComment != null) {
            notificationRepository.save(
                    Notification.builder()
                            .sender(writer)
                            .receiver(parentPostComment.getUser())
                            .content("[ " + post.getTitle() + " ] " + postComment.getContent() + " 댓글에 답글이 달렸어요. 우리 한번 확인해볼까요?")
                            .reason("댓글 생성자: " + writer.getName() + "님")
                            .build()
            );
        }

        return PostCommentResponseDto.from(postComment);
    }

    @Override
    @Transactional
    public PostCommentResponseDto updatePostComment(Long postId, Long commentId, Long departmentId, Long userId, ModifyPostCommentRequestDto requestDto) {

        User writer = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                HttpStatus.NOT_FOUND,
                "존재하지 않는 유저입니다.")
        );

        // TODO 추후 공통 로직으로 변경
        if (writer.getId() != userId) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "작성자가 아닙니다.");
        }

        PostComment postComment = postCommentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                HttpStatus.NOT_FOUND,
                "존재하지 않는 댓글입니다.")
        );

        // TODO 추후 공통 로직으로 변경
        Long commentDepartmentId = postComment.getPost().getBoardCategory().getBoard().getDepartment().getId();
        if (departmentId != commentDepartmentId) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "작성 권한이 없는 게시판입니다.");
        }

        postComment.updateContent(requestDto.getContent());
        postComment.updateCommentOrder(requestDto.getCommentOrder());

        postCommentRepository.save(postComment);

        return PostCommentResponseDto.from(postComment);
    }

    @Override
    @Transactional
    public PostCommentResponseDto deletePostComment(Long postId, Long commentId, Long departmentId, Long userId) {

        User writer = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                HttpStatus.NOT_FOUND,
                "존재하지 않는 유저입니다.")
        );

        // TODO 추후 공통 로직으로 변경
        if (writer.getId() != userId) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "작성자가 아닙니다.");
        }

        PostComment postComment = postCommentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                HttpStatus.NOT_FOUND,
                "존재하지 않는 댓글입니다.")
        );

        // TODO 추후 공통 로직으로 변경
        Long commentDepartmentId = postComment.getPost().getBoardCategory().getBoard().getDepartment().getId();
        if (departmentId != commentDepartmentId) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "작성 권한이 없는 게시판입니다.");
        }

        postCommentRepository.delete(postComment);

        return PostCommentResponseDto.from(postComment);
    }
}
