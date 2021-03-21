#####  编写查询User对象的实现

schema {  #定义查询
    query: UserQuery
}

type UserQuery{
    user: User   #指定对象以及参数类型
}

type User{
    id: Long!     #! 表示该属性是非空项
    name: String
    age: Int
}