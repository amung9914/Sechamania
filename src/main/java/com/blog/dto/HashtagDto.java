package com.blog.dto;

import lombok.Data;

@Data
public class HashtagDto {
    private AddArticleDto dto;
    private String[] hashtags;

    public HashtagDto(AddArticleDto dto, String[] hashtags) {
        this.dto = dto;
        this.hashtags = hashtags;
    }

    /*
    * 클라이언트에서 요청 보낼때 JSON구조
    *
        {
          "contentDTO": {
            "title": "게시글 제목",
            "content": "게시글 내용입니다.",
            "categoryId": 123
          },
          "hashtags": ["java", "spring", "programming"]
        }
    */
}
