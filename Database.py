from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate
from datetime import datetime
migrate = Migrate(app, db)

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///tasks.db'
db = SQLAlchemy(app)

print("Hello User")

class Person:
  def __init__(self, fname, lname):
    self.firstname = fname
    self.lastname = lname

  def printname(self):
    print(self.firstname, self.lastname)

 # User Class
class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(10), unique=True, nullable=False)
    email = db.Column(db.String(30), unique=True, nullable=False)
    password_hash = db.Column(db.String(20), nullable=False) # Password has max 20 characters

# Task Class
class Task(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    title = db.Column(db.String(100), nullable=False)
    description = db.Column(db.Text)
    priority = db.Column(db.Integer)
    deadline = db.Column(db.Date)
    status = db.Column(db.String(20), default='pending')
# Projects class
class Project(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(200), nullable = False) # Column must include text and no longer than 200 characters
    description = db.Column(db.Text, nullable = False) # Can allow strings and cant be blank
    tasks = db.relationships('Task', backref='project', lazy = True) # one project can have many tasks, 

# Comment Class for leaving comments
class Comment(db.model):
    id = db.Column(db.Integer, primary_key=True)
    task_id = db.Column(db.Integer, db.ForeignKey('task.id'), nullable=False)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    content = db.Column(db.Text, nullable = False)
    timestamp = db.Column(db.DateTime, default=datetime.estnow)

@app.route('/tasks', methods=['GET'])
def get_tasks():
    tasks = Task.query.all()
    return jsonify([task.title for task in tasks])

@app.route('/tasks', methods=['POST'])
def add_task():
    data = request.get_json()
    new_task = Task(
        user_id=data['user_id'],
        title=data['title'],
        description=data.get('description'),
        priority=data.get('priority'),
        deadline=data.get('deadline')
    )
    db.session.add(new_task)
    db.session.commit()
    return jsonify({'message': 'Task created successfully'}), 201

@app.route('/tasks/<int:id>', methods=['PUT'])
def update_task(id):
    data = request.get_json()
    task = Task.query.get(id)
    if not task:
        return jsonify({'message': 'Task not found'}), 404
    task.title = data.get('title', task.title)
    task.description = data.get('description', task.description)
    task.priority = data.get('priority', task.priority)
    task.deadline = data.get('deadline', task.deadline)
    task.status = data.get('status', task.status)
    db.session.commit()
    return jsonify({'message': 'Task updated successfully'})

@app.route('/tasks/<int:id>', methods=['DELETE'])
def delete_task(id):
    task = Task.query.get(id)
    if not task:
        return jsonify({'message': 'Task not found'}), 404
    db.session.delete(task)
    db.session.commit()
    return jsonify({'message': 'Task deleted successfully'})

@app.route('/projects', methods=['GET'])
def get_projects():
    projects = Project.query.all()
    return jsonify([{'id': project.id, 'name': project.name, 'description': project.description} for project in projects])

@app.route('/projects', methods=['POST'])
def add_project():
    data = request.get_json()
    new_project = Project(name=data['name'], description=data.get('description'))
    db.session.add(new_project)
    db.session.commit()
    return jsonify({'message': 'Project created successfully'}), 201

@app.route('/projects/<int:id>', methods=['GET'])
def get_project(id):
    project = Project.query.get(id)
    if not project:
        return jsonify({'message': 'Project not found'}), 404
    return jsonify({'id': project.id, 'name': project.name, 'description': project.description})

@app.route('/projects/<int:id>', methods=['PUT'])
def update_project(id):
    data = request.get_json()
    project = Project.query.get(id)
    if not project:
        return jsonify({'message': 'Project not found'}), 404
    project.name = data.get('name', project.name)
    project.description = data.get('description', project.description)
    db.session.commit()
    return jsonify({'message': 'Project updated successfully'})

@app.route('/projects/<int:id>', methods=['DELETE'])
def delete_project(id):
    project = Project.query.get(id)
    if not project:
        return jsonify({'message': 'Project not found'}), 404
    db.session.delete(project)
    db.session.commit()
    return jsonify({'message': 'Project deleted successfully'})

@app.route('/comments', methods=['POST'])
def add_comment():
    data = request.get_json()
    new_comment = Comment(
        task_id=data['task_id'],
        user_id=data['user_id'],
        content=data['content']
    )
    db.session.add(new_comment)
    db.session.commit()
    return jsonify({'message': 'Comment added successfully'}), 201

@app.route('/comments/<int:task_id>', methods=['GET'])
def get_comments(task_id):
    comments = Comment.query.filter_by(task_id=task_id).all()
    return jsonify([{'id': comment.id, 'user_id': comment.user_id, 'content': comment.content, 'timestamp': comment.timestamp} for comment in comments])

@app.route('/comments/<int:id>', methods=['PUT'])
def update_comment(id):
    data = request.get_json()
    comment = Comment.query.get(id)
    if not comment:
        return jsonify({'message': 'Comment not found'}), 404
    comment.content = data.get('content', comment.content)
    db.session.commit()
    return jsonify({'message': 'Comment updated successfully'})

@app.route('/comments/<int:id>', methods=['DELETE'])
def delete_comment(id):
    comment = Comment.query.get(id)
    if not comment:
        return jsonify({'message': 'Comment not found'}), 404
    db.session.delete(comment)
    db.session.commit()
    return jsonify({'message': 'Comment deleted successfully'})

if __name__ == '__main__':
    db.create_all()
    app.run(debug=True)
