package com.blog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTraffic is a Querydsl query type for Traffic
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTraffic extends EntityPathBase<Traffic> {

    private static final long serialVersionUID = 1169344177L;

    public static final QTraffic traffic = new QTraffic("traffic");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath referralPath = createString("referralPath");

    public final DateTimePath<java.time.LocalDateTime> visitDate = createDateTime("visitDate", java.time.LocalDateTime.class);

    public QTraffic(String variable) {
        super(Traffic.class, forVariable(variable));
    }

    public QTraffic(Path<? extends Traffic> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTraffic(PathMetadata metadata) {
        super(Traffic.class, metadata);
    }

}

