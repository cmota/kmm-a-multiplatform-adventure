//
//  ConferencesViewController.swift
//  iosApp
//

import UIKit
import shared
import FirebaseFirestore

let imageCache = NSCache<AnyObject, AnyObject>()

class ConferencesViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    
    private var conferences = [Conference]()
    
    private let presenterConference = ServiceLocator.init().getConferencePresenter
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.separatorStyle = .none
    }
    
    override func viewDidAppear(_ animated: Bool) {
        presenterConference.attachView(currView: self)
    }
      
    override func viewWillDisappear(_ animated: Bool) {
        presenterConference.detachView()
    }
}

extension ConferencesViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return conferences.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let conference = conferences[indexPath.row]
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "ConferenceCell", for: indexPath) as! ConferenceCell
        let placeSeparator = conference.city.count > 0 && conference.country.count > 0 ? ", " : ""
        
        cell.titleLabel.text = conference.name
        cell.subtitleLabel.text = "\(conference.city)\(placeSeparator)\(conference.country)"
        cell.dateLabel.text = conference.date
        cell.statusTextView.text = conference.status
        cell.statusTextView.layer.cornerRadius = 10
        cell.asyncImageView.imageFromServerURL(url: conference.logo)
        
        if conference.status == "online" {
            cell.statusTextView.backgroundColor = #colorLiteral(red: 0.0862745098, green: 0.1450980392, blue: 0.5176470588, alpha: 1)
        }
        else {
            cell.statusTextView.backgroundColor = #colorLiteral(red: 0.8235294118, green: 0.3921568627, blue: 0.2509803922, alpha: 1)
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didDeselectRowAt indexPath: IndexPath) {
        let conference = conferences[indexPath.row]
        
        guard let url = URL(string: conference.website) else {
            return
        }
        
        UIApplication.shared.open(url)
    }
}

extension ConferencesViewController: IConferenceData {
  
   func onConferenceDataFailed(e: KotlinException) {
       Gutenberg().w(tag: "onConferenceDataFailed", message: "Error:\(e)")
   }
  
   func onConferenceDataFetched(conferences: [Conference]) {
       for conference in conferences {
           Gutenberg().d(tag: "onConferenceDataFetched", message: conference.name)
       }
      
       self.conferences = conferences
       self.tableView.reloadData()
   }
}

class ConferenceCell: UITableViewCell {
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var subtitleLabel: UILabel!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var statusTextView: UITextView!
    @IBOutlet weak var asyncImageView: AsyncImageView!
}

class AsyncImageView: UIImageView {
    
    private var currentUrl: String?
    
    public func imageFromServerURL(url: String) {
        currentUrl = url
        
        let sessionConfig = URLSessionConfiguration.default
        let session = URLSession(configuration: sessionConfig, delegate: nil, delegateQueue: nil)
        
        guard let sessionUrl = URL(string: url) else {
            return
        }
        
        // retrieves image if already available in cache
        if let imageFromCache = imageCache.object(forKey: url as AnyObject) as? UIImage {
            
            self.image = imageFromCache
            return
        }
        
        let task = session.dataTask(with: sessionUrl) { (data, response, error) in
            
            if error == nil {
                
                DispatchQueue.main.async {
                    
                    guard
                        let data = data,
                        let downloadedImage = UIImage(data: data),
                        url == self.currentUrl else {
                        
                        return
                        
                    }
                    
                    self.image = downloadedImage
                    imageCache.setObject(downloadedImage, forKey: url as AnyObject)
                }
            }
        }
        
        self.image = nil
        task.resume()
    }
}
