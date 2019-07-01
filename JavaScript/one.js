var person = {
  firstName: "milackron",
  lastName : "tg",
  id       : 47,
  fullName : function() {
    return this.firstName + " " + this.lastName+" "+this.id;
  }
};
document.write(person["firstName"]);
document.write("<br>")
document.write(person["id"])
