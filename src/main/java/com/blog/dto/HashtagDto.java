package com.blog.dto;

import com.blog.entity.Article;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HashtagDto {
    private AddArticleDto addArticleDto;
    private String[] hashtags;

    public HashtagDto(AddArticleDto addArticleDto, String[] hashtags) {
        this.addArticleDto = addArticleDto;
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
