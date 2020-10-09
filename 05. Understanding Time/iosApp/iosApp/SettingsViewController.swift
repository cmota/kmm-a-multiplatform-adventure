//
//  SettingsViewController.swift
//  iosAppx
//

import UIKit
import shared

class SettingsViewController: UIViewController {    
    
    @IBOutlet weak var toggleOnline: UISwitch!
    @IBOutlet weak var editTextUsername: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    
        let online = PlatformSettings.init().settingsRepository.shouldShowOnlyOnlineConferences()
        toggleOnline.setOn(online, animated: true)
        toggleOnline.addTarget(self, action: #selector(onSwitchValueChanged), for: .touchUpInside)
            
        let username = PlatformSettings.init().settingsRepository.getUsername()
        editTextUsername.text = username
        editTextUsername.addTarget(self, action: #selector(onTextFieldValueChanged(_:)), for: .editingChanged)
    }
        
    @objc func onSwitchValueChanged(_ toggleOnline: UISwitch) {
        Gutenberg().d(tag: "onSwitchValueChanged", message:"\(toggleOnline.isOn)")
        PlatformSettings.init().settingsRepository.onlyOnlineConferences(state: toggleOnline.isOn)
    }
        
    @objc func onTextFieldValueChanged(_ textField: UITextField) {
        let username = editTextUsername.text ?? ""
        Gutenberg().d(tag: "onTextFieldValueChanged", message: username)
        PlatformSettings.init().settingsRepository.setUsername(username: username)
    }
}
