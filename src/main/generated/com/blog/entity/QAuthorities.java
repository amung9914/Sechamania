package com.blog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuthorities is a Querydsl query type for Authorities
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthorities extends EntityPathBase<Authorities> {

    private static final long serialVersionUID = -1722401323L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuthorities authorities = new QAuthorities("authorities");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final QMember member;

    public final StringPath role = createString("role");

    public QAuthorities(String variable) {
        this(Authorities.class, forVariable(variable), INITS);
    }

    public QAuthorities(Path<? extends Authorities> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuthorities(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuthorities(PathMetadata metadata, PathInits inits) {
        this(Authorities.class, metadata, inits);
    }

    public QAuthorities(Class<? extends Authorities> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

