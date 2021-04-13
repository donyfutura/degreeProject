package com.example.comparator;

import com.example.api.response.CommentDto;

import java.util.Comparator;

public class CommentsComparator implements Comparator<CommentDto> {
    @Override
    public int compare(CommentDto o1, CommentDto o2) {
        return (int) (o1.getTimestamp() - o2.getTimestamp());
    }
}
