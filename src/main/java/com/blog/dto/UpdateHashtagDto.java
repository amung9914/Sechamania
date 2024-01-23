package com.blog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateHashtagDto {
    private AddArticleDto dto;
    private String[] hashtags;
    private long articleId;

    public UpdateHashtagDto(AddArticleDto dto, String[] hashtags, long articleId) {
        this.dto = dto;
        this.hashtags = hashtags;
        this.articleId = articleId;
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
