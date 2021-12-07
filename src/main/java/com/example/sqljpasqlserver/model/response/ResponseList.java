package com.example.sqljpasqlserver.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseList<T> {
    private Boolean successfull;
    private List<T> result;
    private Integer errorCode;
    private String errorMessage;

    public ResponseList(Boolean successfull, List<T> result, Integer errorCode, String errorMessage) {
        this.successfull = successfull;
        this.result = result;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    //bổ xung thêm trang khi trả về llist
    private Integer currentPage;
    private Long totalItems;
    private int totalPages;


}
