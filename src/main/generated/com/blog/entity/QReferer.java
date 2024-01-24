package com.blog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReferer is a Querydsl query type for Referer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReferer extends EntityPathBase<Referer> {

    private static final long serialVersionUID = -973242911L;

    public static final QReferer referer = new QReferer("referer");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath path = createString("path");

    public QReferer(String variable) {
        super(Referer.class, forVariable(variable));
    }

    public QReferer(Path<? extends Referer> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReferer(PathMetadata metadata) {
        super(Referer.class, metadata);
    }

}

