package org.board.board.service;

import org.board.board.dto.board.BoardCreateRequest;
import org.board.board.dto.board.BoardCreateResponse;
import org.board.board.entity.Board;
import org.board.board.repository.BoardRepository;
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
}
