var mongoose = require('mongoose');
var Schema = mongoose.Schema, _id = Schema.ObjectID;

var beanschema = new Schema({
    bookID: { type: String, require: true },
    code: { type: Number, require: true },
    message: { type: String, require: true },
    data: {
        total: { type: Number, require: true },
        list: [{
            id: { type: Number, require: true },
            nickName: { type: String, require: true },
            userLogo: { type: String, require: true },
            content: { type: String, require: true },
            imgId: { type: String, require: true },
            replyTotal: { type: Number, require: true },
            createDate: { type: String, require: true },
            replyList: [{
                nickName: { type: String, require: true },
                userLogo: { type: String },
                id: { type: Number, require: true },
                commentId: { type: String, require: true },
                content: { type: String, require: true },
                status: { type: String, require: true },
                createDate: { type: String, require: true },

            }],
        }]
    }

});
var bean = mongoose.model('bean', beanschema);
module.exports = { bean };
