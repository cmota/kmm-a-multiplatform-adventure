//
//  ChatViewController.swift
//  iosApp
//

import UIKit
import shared
import FirebaseFirestore

class ChatViewController: UIViewController {
    
    let collection = ValuesKt.COLLECTION_DCEMEA
    
    @IBOutlet weak var textField: UITextField!
    @IBOutlet weak var chatInputView: UIView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var sendButton: UIButton!
    @IBOutlet weak var chatInputViewBottomConstraint: NSLayoutConstraint!
    
    private var messages = [Message]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide), name: UIResponder.keyboardWillHideNotification, object: nil)
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.separatorStyle = .none
        
        textField.delegate = self

        loadMessages()
    }
    @IBAction func didTapOnSendButton(_ sender: Any) {
        
        guard let message = textField.text else {
            return
        }
        
        sendMessage(message: message)
        textField.text = ""
    }
    
    private func scrollToBottom(animated: Bool) {
        
        let indexPath = IndexPath(row: messages.count - 1, section: 0)
        tableView.scrollToRow(at: indexPath, at: .top, animated: animated)
    }
    
    private func dismissKeyboard() {
        
        self.textField.resignFirstResponder()
    }
}

extension ChatViewController
{
    private func loadMessages() {
        PlatformFirebaseFirestoreKt.getFirebaseInstance().collection(collection).addSnapshotListener { snapshot, error in
            
            guard let snapshot = snapshot else {
                Gutenberg().d(tag: "ChatViewController", message: "Error fetching document: \(error!)")
                return
            }
            
            self.messages.removeAll()
            
            for document in snapshot.documents {
                                
                let userId = document.data()["id"] as? String ?? ""
                let username = document.data()["username"] as? String ?? ""
                let content = document.data()["content"] as? String ?? ""
                let timestamp = document.data()["timestamp"] as? String ?? ""
                
                let message = Message(id: userId, user: username, content: content, timestamp: timestamp, outgoing: false)
                
                self.messages.append(message)
                
                Gutenberg().d(tag: "ChatViewController", message: "user=\(document.data()["username"]!) with message: \(document.data()["content"]!)")
            }
            
            self.messages.sort { $0.timestamp < $1.timestamp }
            
            self.tableView.reloadData()
            self.scrollToBottom(animated: false)
        }
    }
    
    private func sendMessage(message: String) {
        ServiceLocator.init().getMessagesPresenter.sendMessage(message: message)
    }
}

extension ChatViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return messages.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let message = messages[indexPath.row]
        
        let cell = tableView.dequeueReusableCell(withIdentifier: !message.outgoing ? "IncomingMessageCell" : "OutgoingMessageCell", for: indexPath) as! ChatMessageCell
        
        cell.updateWithName(!message.outgoing ? message.user : "", message: message.content, isIncoming: !message.outgoing)
        
        return cell
    }
}

extension ChatViewController: UITextFieldDelegate {
    
    
}

extension ChatViewController {
    
    @objc
    func keyboardWillShow(notification: Notification) {
        guard let info = notification.userInfo else { return }
        guard let frameInfo = info[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue else { return }
        var constant = frameInfo.cgRectValue.height
        
        if #available(iOS 11.0, *) {
            let window = UIApplication.shared.windows.filter {$0.isKeyWindow}.first
            constant -= window?.safeAreaInsets.bottom ?? 0
            constant -= window?.safeAreaInsets.top ?? 0
        }
        
        let time = DispatchTime.now() + 0.1
            
        DispatchQueue.main.asyncAfter(deadline: time, execute: {
            self.scrollToBottom(animated: true)
        })
        
        UIView.animate(withDuration: 0.5) {
            self.chatInputViewBottomConstraint.constant = constant
        
        }
    }
    
    @objc
    func keyboardWillHide(notification: Notification) {
        
        UIView.animate(withDuration: 0.5) {
            self.chatInputViewBottomConstraint.constant = 0
        }
    }
}

class ChatMessageCell: UITableViewCell {
    
    let incomingColor = #colorLiteral(red: 0.8973231316, green: 0.897726953, blue: 0.9190878272, alpha: 1)
    let outgoingColor = #colorLiteral(red: 0.1668786705, green: 0.5745041966, blue: 0.9792514443, alpha: 1)
    
    @IBOutlet weak var name: UILabel!
    @IBOutlet weak var messageTextField: UITextView!
    
    @IBOutlet weak var messageTextFieldLeadingConstraint: NSLayoutConstraint!
    @IBOutlet weak var messageTextFieldTrailingConstraint: NSLayoutConstraint!
    
    public var isIncoming = false
    
    public func updateWithName(_ name: String, message: String, isIncoming: Bool) {
        
        self.name.text = name
        self.messageTextField.text = message
        
        let padding = self.contentView.frame.size.width - self.contentView.frame.size.width * 0.8
        
        if isIncoming {
            messageTextFieldLeadingConstraint.constant = 10
            messageTextFieldTrailingConstraint.constant = padding
            self.messageTextField.backgroundColor = incomingColor
            self.messageTextField.textColor = UIColor.black
        }
        else {
            messageTextFieldLeadingConstraint.constant = padding
            messageTextFieldTrailingConstraint.constant = 10
            self.messageTextField.backgroundColor = outgoingColor
            self.messageTextField.textColor = UIColor.white
        }
        
        messageTextField.layer.cornerRadius = 10
    }
}
