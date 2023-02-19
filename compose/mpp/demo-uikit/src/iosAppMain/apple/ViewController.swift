import UIKit
import shared

class ViewController: UIViewController {

    private let contactButton: UIButton = {
        let button = UIButton()
        button.setTitle("Go to Compose", for: .normal)
        button.setTitleColor(.black, for: .normal)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor.green
        title = "CustomNavBar"// Navigation title

        createCustomNavigationBar()
        let audioRightButton = createCustomButton(
                imageName: "phone",
                selector: #selector(buttonHandler)
        )
        let videoRightButton = createCustomButton(
                imageName: "video",
                selector: #selector(buttonHandler)
        )
        let customTitleView = createCustomTitleView(
                contactName: "Swiftbook",
                contactDescription: "New lesson...",
                contactImage: "swift"
        )
        navigationItem.rightBarButtonItems = [audioRightButton, videoRightButton]
        navigationItem.titleView = customTitleView
        contactButton.addTarget(self, action: #selector(buttonHandler), for: .touchUpInside)
        view.addSubview(contactButton)
        setContraints()
    }

    @objc private func buttonHandler() {
        print("Navigation navigationItem buttonHandler")
        // Compose UIViewController
        let contactViewController = SwiftHelper().getViewController()
        navigationController?.pushViewController(contactViewController, animated: true)
    }

    private func setContraints() {
        NSLayoutConstraint.activate([
            contactButton.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            contactButton.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            contactButton.heightAnchor.constraint(equalToConstant: 50),
            contactButton.widthAnchor.constraint(equalToConstant: 150)
        ])
    }
}

extension UIViewController {

    func createCustomNavigationBar() {
        navigationController?.navigationBar.barTintColor = #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)
    }

    func createCustomTitleView(contactName: String, contactDescription: String, contactImage: String) -> UIView {
        let view = UIView()
        view.frame = CGRect(x: 0, y: 0, width: 280, height: 41)

        let imageContact = UIImageView()
        imageContact.image = UIImage(named: contactImage)
        imageContact.layer.cornerRadius = imageContact.frame.height / 2
        imageContact.frame = CGRect(x: 5, y: 0, width: 40, height: 40)
        view.addSubview(imageContact)

        let nameLabel = UILabel()
        nameLabel.text = contactName
        nameLabel.frame = CGRect(x: 55, y: 0, width: 220, height: 20)
        nameLabel.font = UIFont.systemFont(ofSize: 20)
        view.addSubview(nameLabel)

        let descriptionLabel = UILabel()
        descriptionLabel.text = contactDescription
        descriptionLabel.frame = CGRect(x: 55, y: 21, width: 220, height: 20)
        descriptionLabel.font = UIFont.systemFont(ofSize: 16)
        descriptionLabel.textColor = #colorLiteral(red: 0.6000000238, green: 0.6000000238, blue: 0.6000000238, alpha: 1)
        view.addSubview(descriptionLabel)
        return view
    }

    func createCustomButton(imageName: String, selector: Selector) -> UIBarButtonItem {
        let button = UIButton(type: .system)
        button.setImage(
                UIImage(systemName: imageName)?.withRenderingMode(.alwaysTemplate),
                for: .normal
        )
        button.tintColor = .systemBlue
        button.imageView?.contentMode = .scaleAspectFit
        button.contentVerticalAlignment = .fill
        button.contentHorizontalAlignment = .fill
        button.addTarget(self, action: selector, for: .touchUpInside)

        let menuBarItem = UIBarButtonItem(customView: button)
        return menuBarItem
    }
}
