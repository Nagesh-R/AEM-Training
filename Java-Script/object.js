function Course(title,instructor,level,published,view)
{
    this.title =title;
    this.instructor = instructor;
    this.level = level;
    this.published = published;
    this.view = view;
    this.updateViews = function()
    {
        return ++this.views;
    };
    
}

var course01 = new Course("JavaScript", "Martin", 1, true,0);


console.log(course01);