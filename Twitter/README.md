This is an implementation of a simple Twitter-like application. To this end, I have done the following:

A script create.sql to create tables:
user(uid, name, email)
follows(uid1, uid2)
post(postid, uid, timestamp, text)
comment(commentid, postid, uid, timestamp, text)
 
Used PostgreSQL sequences to create new ids for each post/comment;
these ids are unique across all posts and across all comments, respectively. (There are no comments on comments for simplicity.)

When a user logs in they will see:

---A link to create a post
---A link to see posts by the user
---A link to see posts by other users who are followed by the user.

Implementation Details:
When showing a post, all comments for the post are displayed, and also a comment link.  
The posts are sorted by time, and the comments on a post are displayed just after the post, sorted by time. 
When you click on a comment link you get an interface that lets you add a comment.
All the above interfaces are implemented by the same servlet. Each interface above is implemented by a method called from the common servlet.