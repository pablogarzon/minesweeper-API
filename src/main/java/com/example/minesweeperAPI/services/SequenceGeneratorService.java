package com.example.minesweeperAPI.services;

import java.util.Objects;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.minesweeperAPI.models.DatabaseSequence;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {
	
	private final MongoOperations mongoOperations;

	public long generateSequence(String seqName) {
	    DatabaseSequence counter = mongoOperations.findAndModify(Query.query(Criteria.where("_id").is(seqName)),
	      new Update().inc("seq",1), FindAndModifyOptions.options().returnNew(true).upsert(true),
	      DatabaseSequence.class);
	    return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}
}
