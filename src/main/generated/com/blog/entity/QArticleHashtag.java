package com.blog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticleHashtag is a Querydsl query type for ArticleHashtag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticleHashtag extends EntityPathBase<ArticleHashtag> {

    private static final long serialVersionUID = 324636354L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArticleHashtag articleHashtag = new QArticleHashtag("articleHashtag");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final QArticle article;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QHashtag hashtag;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public QArticleHashtag(String variable) {
        this(ArticleHashtag.class, forVariable(variable), INITS);
    }

    public QArticleHashtag(Path<? extends ArticleHashtag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArticleHashtag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArticleHashtag(PathMetadata metadata, PathInits inits) {
        this(ArticleHashtag.class, metadata, inits);
    }

    public QArticleHashtag(Class<? extends ArticleHashtag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.article = inits.isInitialized("article") ? new QArticle(forProperty("article"), inits.get("article")) : null;
        this.hashtag = inits.isInitialized("hashtag") ? new QHashtag(forProperty("hashtag")) : null;
    }

}

