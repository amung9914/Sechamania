package com.blog.service;

import com.blog.entity.ArticleHashtag;
import com.blog.entity.Hashtag;
import com.blog.repository.ArticleHashtagRepository;
import com.blog.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final ArticleHashtagRepository articleHashtagRepository;

    public List<Hashtag> findAll(){
        return hashtagRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }

    public void delete(String tagName){
        Hashtag hashtag = hashtagRepository.findByname(tagName)
                        .orElseThrow(()->new IllegalArgumentException("존재하지 않는 Hashtag입니다"));
        List<ArticleHashtag> list = articleHashtagRepository.findAllByHashtag(hashtag);
        boolean flag = true;
        for (ArticleHashtag articleHashtag : list) {
            if(articleHashtag.getArticle()!=null){
                flag = false;
                break;
            }
        }
        if(flag){
            hashtagRepository.delete(hashtag);
        }else{
            throw new IllegalStateException("게시물이 존재하는 hashtag입니다");
        }

    }
}
