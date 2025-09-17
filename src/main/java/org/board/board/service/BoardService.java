package org.board.board.service;

import java.util.List;
import java.util.stream.Collectors;

import org.board.board.dto.board.BoardCreateRequest;
import org.board.board.dto.board.BoardCreateResponse;
import org.board.board.dto.board.BoardListResponse;
import org.board.board.entity.Board;
import org.board.board.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
  private final BoardRepository boardRepository;

  public BoardService(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  public BoardCreateResponse createBoard(BoardCreateRequest request, Long memberId) {
    Board board = new Board(request.getTitle(), request.getContent(), memberId);

    Board savedBoard = boardRepository.save(board);

    return new BoardCreateResponse(
        savedBoard.getId(),
        savedBoard.getTitle(),
        savedBoard.getContent(),
        savedBoard.getMemberId());
  }

  public Board getBoardById(Long id) {
    return boardRepository.findById(id).orElse(null);
  }

  public Page<BoardListResponse> getBoardsList(Pageable pageable) {
    Page<Board> boardPage = boardRepository.findAll(pageable);

    List<BoardListResponse> boardListResponses =
        boardPage.getContent().stream().map(BoardListResponse::from).collect(Collectors.toList());

    return new PageImpl<>(boardListResponses, pageable, boardPage.getTotalElements());
  }

  public Board updateBoard(Long id, String title, String content, Long memberId) {
    Board board =
        boardRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Board not found"));
    if (!board.getMemberId().equals(memberId)) {
      throw new IllegalArgumentException("You are not the author of this post.");
    }
    board.setTitle(title);
    board.setContent(content);
    return boardRepository.save(board);
  }

  public void deleteBoard(Long id, Long memberId) {
    Board board =
        boardRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Board not found"));
    if (!board.getMemberId().equals(memberId)) {
      throw new IllegalArgumentException("You are not the author of this post.");
    }
    boardRepository.deleteById(id);
  }
}
